---
name: spring-api-rules
description: Define controllers, services, repositories, entities, DTOs for Spring Boot + Kotlin DDD project. Use when user mentions API, endpoint, controller, service, repository, entity, DTO, CRUD, domain, feature, function, or REST creation.
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# Spring Boot + Kotlin DDD 개발 규칙

이 프로젝트의 표준 개발 규칙입니다.

## 패키지 구조

```
org.example.studycafe
├── branch/                    # Bounded Context: 지점
│   ├── domain/                # Entity
│   ├── application/           # Service
│   │   └── dto/               # Command, SearchCondition
│   ├── infrastructure/        # JpaRepository, QueryDSL
│   └── presentation/          # Controller
│       ├── request/           # Request DTO
│       └── response/          # Response DTO
├── member/                    # Bounded Context: 회원
│   └── (branch와 동일 구조)
├── admin/                     # Bounded Context: 관리자
│   └── (branch와 동일 구조)
└── common/
    ├── config/                # Spring 설정
    ├── domain/                # BaseDateTimeEntity 등 공유 base 클래스
    └── enumeration/           # 프로젝트 전체 Enum 통합 관리
```

## 공통 규칙

- DI는 생성자 주입만 사용한다 (Kotlin primary constructor로 자동 생성자 주입).
- `@Autowired` 필드 주입 금지.
- `@ConfigurationProperties` 클래스는 `data class`로 작성한다.
- Lombok 사용 금지 (Kotlin 언어 기능으로 대체).

## Controller

- `@RestController` 사용.
- 반환 타입: `ResponseEntity<T>`.
- 클래스 레벨 `@RequestMapping` 사용 가능 (경로 prefix 용도).
- 네이밍: `*Controller`.
- Service만 주입받는다. Repository 직접 주입 금지.
- Domain 객체(`@Entity`)를 직접 반환하지 않고 Response DTO로 변환한다.
- 입력 검증은 `@Valid` + Request DTO에서 처리한다.

```kotlin
@RestController
@RequestMapping("/api/v1/branches")
class BranchController(
    private val branchService: BranchService,
) {
    @GetMapping("/{id}")
    fun getBranch(@PathVariable id: Long): ResponseEntity<BranchResponse> {
        val branch = branchService.getBranch(id)
        return ResponseEntity.ok(BranchResponse.from(branch))
    }
}
```

## DTO

### Request DTO (`presentation/request/`)

- `data class`로 작성한다.
- 입력 검증: Bean Validation 어노테이션 (`@field:NotBlank` 등).
- `toCommand()` 또는 `toCondition()` 변환 메서드를 내부에 작성한다.

```kotlin
data class BranchCreateRequest(
    @field:NotBlank(message = "지점명은 필수입니다.")
    val name: String,
) {
    fun toCommand() = BranchCreateCommand(name = name)
}
```

### Response DTO (`presentation/response/`)

- `data class`로 작성한다.
- `companion object`에 `from(entity)` 팩토리 메서드를 작성한다.
- DTO에 비즈니스 로직을 작성하지 않는다 (데이터 변환만 허용).

```kotlin
data class BranchResponse(val id: Long, val name: String) {
    companion object {
        fun from(branch: Branch) = BranchResponse(
            id = requireNotNull(branch.id),
            name = requireNotNull(branch.name),
        )
    }
}
```

### Command / SearchCondition (`application/dto/`)

- `data class`로 작성한다.
- Command: 생성/수정 입력 객체 (presentation → application 전달용).
- SearchCondition: 목록 조회 조건 객체 (QueryDSL에 전달).

## Domain (Entity)

- `@Entity` 클래스는 `{context}/domain/` 에 위치한다.
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` 필수.
- ID는 `var id: Long? = null` 형태로 선언한다.
- `allOpen` 플러그인 적용으로 Kotlin의 `final` 제약 해소됨 (`build.gradle.kts` 설정).
- 비즈니스 규칙은 Entity 메서드로 캡슐화한다 (Service에 로직 두지 않음).
- 다른 Bounded Context의 Entity를 `@ManyToOne`으로 참조하지 않는다. ID(Long)로만 참조한다.
- 연관관계: `FetchType.LAZY` 기본 사용.

```kotlin
@Entity
class Branch(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,
) {
    fun rename(newName: String) {
        require(newName.isNotBlank()) { "지점명은 비어있을 수 없습니다." }
        this.name = newName
    }
}
```

## Repository (`infrastructure/`)

- `JpaRepository<Entity, Long>` 상속.
- 커스텀 동적 조회가 필요하면 QueryDSL 패턴 사용:
  - `{Entity}RepositoryCustom` 인터페이스 선언
  - `{Entity}RepositoryImpl` 구현체 작성 (JPAQueryFactory 사용)
  - `{Entity}Repository`에서 두 인터페이스 함께 상속
- 단순 조회는 Spring Data JPA 메서드 네이밍 쿼리 사용.
- 동적 조건 2개 이상이면 QueryDSL 사용.

```kotlin
// Custom 인터페이스
interface BranchRepositoryCustom {
    fun search(condition: BranchSearchCondition): List<Branch>
}

// QueryDSL 구현체 — 반드시 {Repository명}Impl 네이밍
class BranchRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : BranchRepositoryCustom {
    override fun search(condition: BranchSearchCondition): List<Branch> {
        val branch = QBranch.branch
        return queryFactory.selectFrom(branch)
            .where(condition.name?.let { branch.name.containsIgnoreCase(it) })
            .fetch()
    }
}

// 통합
interface BranchRepository : JpaRepository<Branch, Long>, BranchRepositoryCustom
```

## Service (`application/`)

- `@Service` + 클래스 레벨 `@Transactional(readOnly = true)` 기본 선언.
- 쓰기 메서드에는 `@Transactional` 별도 선언.
- 비즈니스 로직을 Service에 직접 작성하지 않고 Entity 메서드에 위임한다.
- 다른 Bounded Context의 Repository를 직접 주입받지 않는다.

```kotlin
@Service
@Transactional(readOnly = true)
class BranchService(
    private val branchRepository: BranchRepository,
) {
    @Transactional
    fun create(command: BranchCreateCommand): Branch {
        val branch = Branch(name = command.name)
        return branchRepository.save(branch)
    }
}
```

## Enum

- 모든 Enum은 `common/enumeration/` 에 통합 관리한다.
- 이름에 Context 정보를 포함한다 (`BranchSeatType`, `GenderType` 등).

## 예외 처리

- Domain 예외: `{context}/domain/{Name}Exception.kt`
- 전역 처리: `common/` 아래 `@RestControllerAdvice` 클래스.

## API 테스트 스크립트

새 API 작성 시 `src/main/resources/http/` 에 쉘 스크립트를 함께 작성한다.

- 파일명: 리소스명 소문자 (예: `branch.sh`, `member.sh`)
- `BASE_URL="http://localhost:8080"` 변수 사용
- 인증이 필요한 요청은 `-b cookies.txt` 사용
- 각 curl 명령 앞에 `echo` 설명 추가
- 첫 줄: `#!/bin/bash`
- 모든 엔드포인트(GET, POST, PUT, DELETE) curl 명령 포함
