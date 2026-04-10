# 패키지 구조 마이그레이션: 기술 계층 → DDD Bounded Context

## 변경 배경

기존 구조는 기술 계층(domain, repository) 기준으로 패키지가 분리되어 있었습니다.
DDD(Domain-Driven Design) 방법론 도입에 따라 **Bounded Context(도메인) 기준**으로 패키지를 재편했습니다.

---

## 변경 전 구조

```
studycafe/
├── domain/
│   ├── admin/       Admin, BranchAdmin
│   ├── branch/      Branch, BranchBusinessHour, BranchBusinessHourException,
│   │                BranchPass, BranchPassDetail, BranchPassGroup,
│   │                BranchSeat, BranchSeatGroup, BranchSeatHistory, BranchSeatReservation
│   ├── common/      BaseDateTimeEntity
│   └── member/      Member, MemberBranchPass, MemberBranchPassHistory, MemberPrivacySearch
├── repository/
│   ├── admin/       AdminRepository, BranchAdminRepository
│   ├── branch/      BranchRepository, BranchBusinessHourRepository, ...
│   └── member/      MemberRepository, MemberBranchPassRepository, ...
└── common/
    ├── config/      CorsProperties, SecurityConfig
    └── enumeration/ ← 모든 Enum이 한 곳에 혼재 (13개)
```

---

## 변경 후 구조 (현재)

```
studycafe/
├── branch/                              ← Bounded Context: 지점
│   ├── domain/                          Entity
│   │   ├── Branch.kt
│   │   ├── BranchBusinessHour.kt
│   │   ├── BranchBusinessHourException.kt
│   │   ├── BranchPass.kt
│   │   ├── BranchPassDetail.kt
│   │   ├── BranchPassGroup.kt
│   │   ├── BranchSeat.kt
│   │   ├── BranchSeatGroup.kt
│   │   ├── BranchSeatHistory.kt
│   │   └── BranchSeatReservation.kt
│   ├── application/                     Service, Command, Condition
│   │   ├── BranchService.kt
│   │   ├── BranchCreateCommand.kt
│   │   └── BranchSearchCondition.kt
│   ├── infrastructure/                  JpaRepository + QueryDSL
│   │   ├── BranchRepository.kt          (JpaRepository + Custom 통합)
│   │   ├── BranchRepositoryCustom.kt    (QueryDSL 메서드 인터페이스)
│   │   ├── BranchRepositoryImpl.kt      (QueryDSL 구현체)
│   │   ├── BranchBusinessHourRepository.kt
│   │   ├── BranchBusinessHourExceptionRepository.kt
│   │   ├── BranchPassRepository.kt
│   │   ├── BranchPassGroupRepository.kt
│   │   ├── BranchPassDetailRepository.kt
│   │   ├── BranchSeatRepository.kt
│   │   ├── BranchSeatGroupRepository.kt
│   │   ├── BranchSeatHistoryRepository.kt
│   │   └── BranchSeatReservationRepository.kt
│   └── presentation/                    Controller, DTO
│       ├── BranchController.kt
│       ├── request/
│       │   ├── BranchCreateRequest.kt
│       │   └── BranchSearchRequest.kt
│       └── response/
│           └── BranchResponse.kt
│
├── member/                              ← Bounded Context: 회원
│   ├── domain/
│   │   ├── Member.kt
│   │   ├── MemberBranchPass.kt
│   │   ├── MemberBranchPassHistory.kt
│   │   └── MemberPrivacySearch.kt
│   ├── application/
│   │   └── MemberService.kt
│   ├── infrastructure/
│   │   ├── MemberRepository.kt
│   │   ├── MemberRepositoryCustom.kt
│   │   ├── MemberRepositoryImpl.kt
│   │   ├── MemberBranchPassRepository.kt
│   │   ├── MemberBranchPassHistoryRepository.kt
│   │   └── MemberPrivacySearchRepository.kt
│   └── presentation/
│       ├── MemberController.kt
│       ├── request/
│       │   ├── MemberCreateRequest.kt
│       │   └── MemberSearchRequest.kt
│       └── response/
│           └── MemberResponse.kt
│
├── admin/                               ← Bounded Context: 관리자
│   ├── domain/
│   │   ├── Admin.kt
│   │   └── BranchAdmin.kt
│   ├── application/
│   │   └── AdminService.kt
│   ├── infrastructure/
│   │   ├── AdminRepository.kt
│   │   └── BranchAdminRepository.kt
│   └── presentation/
│       └── AdminController.kt
│
└── common/                              ← 공통
    ├── config/
    │   ├── CorsProperties.kt
    │   └── SecurityConfig.kt
    ├── domain/
    │   └── BaseDateTimeEntity.kt        (domain/common → common/domain 이동)
    └── enumeration/                     ← 모든 Enum을 여기에 통합 관리
        ├── CommonStatus.kt
        ├── BranchBusinessHourExceptionReason.kt
        ├── BranchBusinessHourExceptionType.kt
        ├── BranchSeatGroupType.kt
        ├── BranchSeatReservationStatus.kt
        ├── BranchSeatType.kt
        ├── CalculatorType.kt
        ├── DayOfWeekType.kt
        ├── GenderType.kt
        ├── PassEventType.kt
        ├── PassIssueType.kt
        ├── PassStatus.kt
        └── PassType.kt
```

---

## Enum 배치 결정 및 근거

### 왜 domain/ 안에 두지 않고 common/enumeration/ 에 통합했는가

| 비교 항목 | domain/ 내 배치 | common/enumeration/ 통합 |
|---|---|---|
| Bounded Context 자립성 | 높음 (MSA 분리에 유리) | 낮음 |
| 탐색 편의성 | Enum을 찾으려면 Context 폴더를 알아야 함 | 한 곳에서 모두 확인 가능 |
| 적합한 상황 | MSA 또는 Context가 완전 독립적일 때 | 모노리스, 소규모~중규모 서비스 |

이 프로젝트는 모노리스 구조이며, Enum 이름 자체에 Context 정보가 포함되어 있습니다
(`BranchSeatType`, `GenderType` 등). 따라서 `common/enumeration/`에 통합 관리합니다.

---

## 파일별 이동 매핑 요약

### Domain Entity

| 변경 전 | 변경 후 |
|---|---|
| `domain.branch.*` | `branch.domain` |
| `domain.member.*` | `member.domain` |
| `domain.admin.*` | `admin.domain` |
| `domain.common.BaseDateTimeEntity` | `common.domain.BaseDateTimeEntity` |

### Repository → infrastructure

| 변경 전 | 변경 후 |
|---|---|
| `repository.branch.*` | `branch.infrastructure` |
| `repository.member.*` | `member.infrastructure` |
| `repository.admin.*` | `admin.infrastructure` |

### Enum

모든 Enum → `common.enumeration` (패키지 일원화)

---

## 새로 추가된 레이어 (플레이스홀더)

| 레이어 | 위치 | 역할 |
|---|---|---|
| application | `{context}/application/` | Service, Command, SearchCondition |
| presentation | `{context}/presentation/` | Controller |
| presentation/request | `{context}/presentation/request/` | CreateRequest, SearchRequest |
| presentation/response | `{context}/presentation/response/` | Response DTO |

자세한 각 레이어 역할 및 개발 플로우는 [ddd-development-guide.md](./ddd-development-guide.md)를 참고하세요.
