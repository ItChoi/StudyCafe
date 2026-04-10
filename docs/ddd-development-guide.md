# DDD 개발 정책 및 플로우 가이드

> 이 문서는 StudyCafe 프로젝트에 참여하는 백엔드 개발자가 DDD(Domain-Driven Design) 방법론을 기반으로
> 일관성 있게 개발할 수 있도록 작성된 실무 가이드입니다.

---

## 1. 핵심 개념

### Bounded Context (경계 컨텍스트)

비즈니스 도메인을 독립적으로 책임지는 단위입니다.

| Context | 패키지 | 책임 |
|---|---|---|
| `branch` | `org.example.studycafe.branch` | 지점, 좌석, 이용권 상품, 예약 관리 |
| `member` | `org.example.studycafe.member` | 회원, 회원 이용권, 이용 내역 관리 |
| `admin` | `org.example.studycafe.admin` | 관리자, 지점 관리자 관리 |
| `common` | `org.example.studycafe.common` | 공통 설정, 공유 도메인, 전체 Enum |

### Aggregate Root

연관 객체들의 일관성 경계. 외부에서는 반드시 Aggregate Root를 통해서만 내부에 접근합니다.

| Aggregate Root | 포함 Entity |
|---|---|
| `Branch` | BranchBusinessHour, BranchBusinessHourException, BranchSeatGroup, BranchSeat, BranchPassGroup, BranchPassDetail |
| `BranchSeatReservation` | BranchSeatHistory |
| `Member` | MemberPrivacySearch |
| `MemberBranchPass` | MemberBranchPassHistory |
| `Admin` | BranchAdmin |

---

## 2. 전체 패키지 구조

```
studycafe/
├── {context}/
│   ├── domain/          Entity (핵심 비즈니스 모델)
│   ├── application/     Service + Command + SearchCondition (유스케이스)
│   ├── infrastructure/  JpaRepository + QueryDSL (기술 구현)
│   └── presentation/    Controller + Request DTO + Response DTO (HTTP 처리)
│       ├── request/
│       └── response/
└── common/
    ├── config/          Spring 설정
    ├── domain/          BaseDateTimeEntity 등 공유 base 클래스
    └── enumeration/     프로젝트 전체 Enum (Context 구분 없이 통합 관리)
```

---

## 3. 레이어별 역할과 규칙

### 3-1. domain/

**역할**: 비즈니스 규칙과 상태를 표현하는 순수 도메인 객체

**포함 대상**: `@Entity` 클래스, Value Object

**규칙**:
- 비즈니스 메서드를 Entity 안에 작성한다 (Service에 로직을 두지 않는다).
- 다른 Bounded Context의 `domain/`을 직접 import하지 않는다.
- Context 간 참조는 객체가 아닌 **ID(Long)** 로만 한다.

```kotlin
// 올바른 예 — 도메인 로직을 Entity 안에 캡슐화
@Entity
class MemberBranchPass(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var remainingMinutes: Int,
    @Enumerated(EnumType.STRING)
    var status: PassStatus,
) {
    fun use(minutes: Int) {
        check(status == PassStatus.ACTIVE) { "사용 가능한 이용권이 아닙니다." }
        check(remainingMinutes >= minutes) { "잔여 시간이 부족합니다." }
        remainingMinutes -= minutes
        if (remainingMinutes == 0) status = PassStatus.DEPLETED
    }
}
```

---

### 3-2. application/

**역할**: 유스케이스 단위 비즈니스 흐름 조합. 트랜잭션 경계 담당.

**포함 대상**:

| 파일 | 위치 | 역할 |
|---|---|---|
| `{Entity}Service.kt` | `application/` | 유스케이스 구현 (`@Service`, `@Transactional`) |
| `{Entity}CreateCommand.kt` | `application/dto/` | 생성/수정 입력 객체 (presentation → application) |
| `{Entity}SearchCondition.kt` | `application/dto/` | 목록 조회 조건 객체 (QueryDSL에 전달) |

Service는 유스케이스 **실행체**, Command/Condition은 **데이터 전달 객체**로 역할이 다르므로
`dto/` 서브패키지로 분리하여 일관된 구조를 유지한다.

**규칙**:
- `@Transactional`은 application 레이어에서만 선언한다.
- 조회 전용 메서드에는 `@Transactional(readOnly = true)`를 기본으로 사용한다.
- 도메인 로직을 Service에 직접 작성하지 않고 Entity 메서드에 위임한다.
- 다른 Context의 Repository를 직접 주입받지 않는다.

```kotlin
@Service
@Transactional(readOnly = true)
class MemberBranchPassService(
    private val memberBranchPassRepository: MemberBranchPassRepository,
    private val memberBranchPassHistoryRepository: MemberBranchPassHistoryRepository,
) {
    @Transactional
    fun usePass(command: UsePassCommand) {
        val pass = memberBranchPassRepository.findById(command.passId)
            .orElseThrow { NoSuchElementException("이용권을 찾을 수 없습니다.") }

        pass.use(command.minutes)  // 비즈니스 로직은 Entity에 위임

        memberBranchPassHistoryRepository.save(
            MemberBranchPassHistory(
                memberBranchPassId = pass.id,
                eventType = PassEventType.RESERVE,
                type = CalculatorType.MINUS,
                usedMinutes = command.minutes,
            )
        )
    }
}
```

---

### 3-3. infrastructure/

**역할**: JPA, QueryDSL 등 기술 구현 세부사항

**포함 대상**:

| 파일 | 역할 |
|---|---|
| `{Entity}Repository.kt` | `JpaRepository` + `{Entity}RepositoryCustom` 상속 |
| `{Entity}RepositoryCustom.kt` | 커스텀 조회 메서드 인터페이스 (QueryDSL용) |
| `{Entity}RepositoryImpl.kt` | QueryDSL 구현체 |

**QueryDSL 패턴**:

```kotlin
// 1. 커스텀 인터페이스 선언
interface BranchRepositoryCustom {
    fun search(condition: BranchSearchCondition): List<Branch>
}

// 2. QueryDSL 구현체 — 반드시 {Repository명}Impl로 네이밍
class BranchRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : BranchRepositoryCustom {

    override fun search(condition: BranchSearchCondition): List<Branch> {
        val branch = QBranch.branch  // Gradle 빌드 시 APT가 자동 생성

        return queryFactory
            .selectFrom(branch)
            .where(
                condition.name?.let { branch.name.containsIgnoreCase(it) },
                condition.status?.let { branch.status.eq(it) },
            )
            .fetch()
    }
}

// 3. JpaRepository + Custom 통합
interface BranchRepository : JpaRepository<Branch, Long>, BranchRepositoryCustom
//                                                         ↑ 추가하면 Impl이 자동 연결됨
```

**QueryDSL 흐름**:
```
SearchRequest (presentation)
    → toCondition() → SearchCondition (application)
        → service.search(condition) → repository.search(condition)
            → QueryDSL BooleanExpression 조합 → DB 조회
```

**규칙**:
- 간단한 조회는 `JpaRepository`의 메서드 네이밍 쿼리(`findByXxx`)를 사용한다.
- 동적 조건이 2개 이상이면 QueryDSL을 사용한다.
- `@Query`(JPQL/Native)는 QueryDSL로 표현 불가능한 경우에만 사용한다.
- `SearchCondition`은 `infrastructure/`가 아닌 `application/`에 위치한다 (infrastructure가 presentation을 참조하지 않도록).

---

### 3-4. presentation/

**역할**: HTTP 요청/응답 처리. 도메인 로직은 포함하지 않는다.

**포함 대상**:

| 파일 | 위치 | 역할 |
|---|---|---|
| `{Entity}Controller.kt` | `presentation/` | `@RestController`, 엔드포인트 |
| `{Entity}CreateRequest.kt` | `presentation/request/` | POST/PUT 요청 바디 DTO |
| `{Entity}SearchRequest.kt` | `presentation/request/` | GET 쿼리 파라미터 DTO |
| `{Entity}Response.kt` | `presentation/response/` | 응답 DTO |

**규칙**:
- Controller는 Service만 주입받는다. Repository 직접 주입 금지.
- Domain 객체(`@Entity`)를 그대로 반환하지 않고 Response DTO로 변환한다.
- 입력 검증은 Request DTO에서 `@Valid` + Bean Validation으로 처리한다.
- `toCommand()` / `toCondition()` 변환 메서드를 Request DTO 안에 작성한다.

```kotlin
// Request → Command 변환을 DTO 내부에 캡슐화
data class BranchCreateRequest(
    @field:NotBlank(message = "지점명은 필수입니다.")
    val name: String,
    val address: String,
) {
    fun toCommand() = BranchCreateCommand(name = name, address = address)
}

// Domain → Response 변환을 companion에 캡슐화
data class BranchResponse(val id: Long, val name: String) {
    companion object {
        fun from(branch: Branch) = BranchResponse(
            id = requireNotNull(branch.id),
            name = requireNotNull(branch.name),
        )
    }
}
```

---

## 4. 의존성 방향 규칙

레이어 간 의존성은 반드시 아래 방향으로만 허용합니다.

```
presentation → application → infrastructure → domain
                                  ↑
                             (domain에 직접 접근 가능)
```

**요약**:
- `domain`은 어느 레이어도 import하지 않는다.
- `infrastructure`는 `domain`만 참조한다.
- `application`은 `domain` + `infrastructure`를 참조한다.
- `presentation`은 `application`만 참조한다.
- `SearchCondition`은 `application/`에 위치하여 infrastructure가 presentation을 참조하는 것을 방지한다.

---

## 5. Enum 배치 정책

**모든 Enum은 `common/enumeration/`에 통합 관리합니다.**

- Enum 이름에 Context 정보가 포함되어 있어 (`BranchSeatType`, `GenderType`) 별도 분리 없이 식별 가능합니다.
- 여러 Context에서 공유되는 Enum도 동일 위치에서 관리합니다 (예: `CommonStatus`).

```
common/enumeration/
├── CommonStatus.kt              ← 전 Context 공유
├── BranchSeatType.kt            ← Branch Context 전용이나 동일 위치 관리
├── GenderType.kt                ← Member Context 전용이나 동일 위치 관리
└── ...
```

---

## 6. Context 간 통신 규칙

Bounded Context는 서로 독립적입니다.

### 허용: ID 참조

```kotlin
// member Context에서 branch의 예약 ID를 참조할 때
class MemberBranchPassHistory(
    var reservationId: Long?,  // BranchSeatReservation 객체 참조 X, ID만 보관
)
```

### 허용: Facade (통합 서비스)

두 Context에 걸친 비즈니스 흐름은 `common/application/` 또는 별도 통합 레이어에서 조합합니다.

```kotlin
// common/application/ReservationFacade.kt
@Service
class ReservationFacade(
    private val branchSeatService: BranchSeatService,
    private val memberBranchPassService: MemberBranchPassService,
) {
    @Transactional
    fun reserve(command: ReserveCommand) {
        memberBranchPassService.usePass(...)
        branchSeatService.createReservation(...)
    }
}
```

### 금지

- 다른 Context의 `@Entity`를 `@ManyToOne` 등으로 직접 참조하는 것
- 다른 Context의 Repository를 직접 주입받는 것

---

## 7. 신규 기능 개발 플로우

### Step 1. 도메인 분석

- 어떤 Bounded Context에 속하는지 결정한다.
- 새로운 Entity/Value Object가 필요한지 판단한다.
- 새로운 Enum이 필요하면 `common/enumeration/`에 추가한다.

### Step 2. domain/ 작성

```
{context}/domain/NewEntity.kt
```

- 비즈니스 규칙을 Entity 메서드로 작성한다.
- 다른 Context Entity를 직접 참조하지 않고 ID로만 참조한다.

### Step 3. infrastructure/ 작성

```
{context}/infrastructure/NewEntityRepository.kt
{context}/infrastructure/NewEntityRepositoryCustom.kt  (동적 조회 필요 시)
{context}/infrastructure/NewEntityRepositoryImpl.kt    (동적 조회 필요 시)
```

- 기본 CRUD는 `JpaRepository`가 제공한다.
- 동적 조건 조회는 QueryDSL을 사용한다.

### Step 4. application/ 작성

```
{context}/application/NewEntityService.kt
{context}/application/dto/NewEntityCreateCommand.kt   (생성/수정 입력 필요 시)
{context}/application/dto/NewEntitySearchCondition.kt (목록 조회 필요 시)
```

- `@Transactional`을 적용한다.
- 조회 메서드는 `@Transactional(readOnly = true)` 클래스 레벨 선언을 기본으로 한다.
- 비즈니스 로직은 Entity 메서드를 호출하는 방식으로 위임한다.

### Step 5. presentation/ 작성

```
{context}/presentation/NewEntityController.kt
{context}/presentation/request/NewEntityCreateRequest.kt
{context}/presentation/request/NewEntitySearchRequest.kt
{context}/presentation/response/NewEntityResponse.kt
```

- Request DTO에 `@Valid` + Bean Validation을 추가한다.
- `toCommand()` / `toCondition()` 변환 메서드를 Request DTO 내부에 작성한다.
- `from(entity)` 변환 메서드를 Response DTO의 `companion object`에 작성한다.

---

## 8. 코드 작성 체크리스트

### 새 Entity 추가 시

- [ ] 올바른 Context의 `domain/`에 배치했는가?
- [ ] `@Id`, `@GeneratedValue`가 선언되어 있는가?
- [ ] 비즈니스 규칙이 있다면 Entity 메서드로 작성했는가?
- [ ] 다른 Context Entity를 `@ManyToOne`으로 참조하지 않고 ID만 보관하는가?
- [ ] 해당 Repository를 `infrastructure/`에 추가했는가?

### 새 Service 추가 시

- [ ] `application/` 패키지에 위치하는가?
- [ ] 클래스 레벨에 `@Transactional(readOnly = true)`를 선언했는가?
- [ ] 쓰기 메서드에 `@Transactional`을 별도로 선언했는가?
- [ ] 도메인 로직을 Service에 직접 작성하지 않고 Entity에 위임했는가?
- [ ] 다른 Context의 Repository를 직접 주입받지 않는가?

### 새 Controller 추가 시

- [ ] `presentation/` 패키지에 위치하는가?
- [ ] Repository를 직접 주입받지 않고 Service만 주입받는가?
- [ ] Domain 객체를 직접 반환하지 않고 Response DTO로 변환했는가?
- [ ] 입력값 검증이 Request DTO에 `@Valid`로 선언되어 있는가?
- [ ] `toCommand()` / `toCondition()` 변환이 Request DTO 안에 있는가?

### QueryDSL 추가 시

- [ ] `SearchCondition`이 `application/`에 위치하는가? (`infrastructure/`에 두지 않는다)
- [ ] `{Repository명}RepositoryCustom` 인터페이스를 `infrastructure/`에 선언했는가?
- [ ] `{Repository명}RepositoryImpl`이 Custom 인터페이스를 구현하는가?
- [ ] `{Repository명}Repository`가 JpaRepository와 Custom을 함께 상속하는가?
- [ ] Q클래스가 빌드 후 생성되는지 확인했는가? (`./gradlew build` 또는 `compileJava`)

---

## 9. 파일 네이밍 규칙

| 종류 | 네이밍 예시 |
|---|---|
| Entity | `Branch`, `MemberBranchPass` |
| Enum | `PassStatus`, `GenderType` (common/enumeration/) |
| Repository | `BranchRepository` |
| RepositoryCustom | `BranchRepositoryCustom` |
| RepositoryImpl | `BranchRepositoryImpl` |
| Service | `BranchService`, `MemberBranchPassService` |
| Command | `BranchCreateCommand`, `UsePassCommand` (application/dto/) |
| SearchCondition | `BranchSearchCondition` (application/dto/) |
| Controller | `BranchController` |
| CreateRequest | `BranchCreateRequest` |
| SearchRequest | `BranchSearchRequest` |
| Response | `BranchResponse` |
| Facade (통합) | `ReservationFacade` |
