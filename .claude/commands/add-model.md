# 데이터베이스 모델 생성

인자: `$ARGUMENTS` (형식: `{도메인명} {모델명} [auditing]`)
- 예: `branch BranchRoom` — 기본 Entity + Repository 생성
- 예: `branch BranchRoom auditing` — BaseDateTimeEntity 상속 추가
- `auditing` 옵션: createdDtm / updatedDtm 자동 감사 필드 포함 여부

## 처리 순서

### 1. 인자 파싱
`$ARGUMENTS`를 공백으로 분리한다.
- `DOMAIN` = 첫 번째 인자 (소문자, 예: branch)
- `MODEL` = 두 번째 인자 (PascalCase, 예: BranchRoom)
- `TABLE_NAME` = MODEL을 snake_case로 변환 (예: branch_room)
- `AUDITING` = 세 번째 인자가 `auditing`이면 true, 없으면 false

### 2. 디렉토리 생성
아래 경로가 없으면 생성한다.
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/`
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/domain/`
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/infrastructure/`

### 3. Entity 클래스 생성
경로: `src/main/kotlin/org/example/studycafe/{DOMAIN}/domain/{MODEL}.kt`
파일이 이미 존재하면 생성하지 않는다.

**AUDITING = false 인 경우:**
```kotlin
package org.example.studycafe.{DOMAIN}.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.enumeration.CommonStatus

@Entity
class {MODEL}(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var status: CommonStatus = CommonStatus.ACTIVE,

    // TODO: 필드 추가
)
```

**AUDITING = true 인 경우:**
```kotlin
package org.example.studycafe.{DOMAIN}.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.studycafe.common.domain.BaseDateTimeEntity
import org.example.studycafe.common.enumeration.CommonStatus

@Entity
class {MODEL}(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var status: CommonStatus = CommonStatus.ACTIVE,

    // TODO: 필드 추가
) : BaseDateTimeEntity()
```

**필드 타입 가이드** (TODO 주석 아래에 힌트로 추가):
```kotlin
    // 타입 참고:
    // - 문자열      : var name: String? = null
    // - 숫자        : var amount: Int? = null  /  var price: Long? = null
    // - 날짜+시간   : var startDtm: OffsetDateTime? = null
    // - 날짜만      : var targetDate: LocalDate? = null
    // - 시간만      : var startTime: LocalTime? = null
    // - 불리언      : var isActive: Boolean = false
    // - 다른 테이블 FK : var {target}Id: Long? = null
    // - Enum       : @Enumerated(EnumType.STRING) var type: {EnumType}? = null
```

### 4. Repository 인터페이스 생성
경로: `src/main/kotlin/org/example/studycafe/{DOMAIN}/infrastructure/{MODEL}Repository.kt`
파일이 이미 존재하면 생성하지 않는다.

```kotlin
package org.example.studycafe.{DOMAIN}.infrastructure

import org.example.studycafe.{DOMAIN}.domain.{MODEL}
import org.springframework.data.jpa.repository.JpaRepository

interface {MODEL}Repository : JpaRepository<{MODEL}, Long>
```

### 5. DDL 파일 생성
경로: `docs/ddl/{TABLE_NAME}.sql`
파일이 이미 존재하면 생성하지 않는다.

**AUDITING = false 인 경우:**
```sql
CREATE TABLE {TABLE_NAME}
(
    id     BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    -- TODO: 컬럼 추가
    -- 예시:
    -- name           VARCHAR(100) NOT NULL,
    -- amount         INTEGER,
    -- target_id      BIGINT REFERENCES other_table (id),
    -- start_dtm      TIMESTAMP WITH TIME ZONE,
    -- target_date    DATE,
    -- start_time     TIME,
    -- type           VARCHAR(50),

    CONSTRAINT chk_{TABLE_NAME}_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED'))
);
```

**AUDITING = true 인 경우:**
```sql
CREATE TABLE {TABLE_NAME}
(
    id          BIGSERIAL PRIMARY KEY,
    status      VARCHAR(20)              NOT NULL DEFAULT 'ACTIVE',

    -- TODO: 컬럼 추가

    created_dtm TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_dtm TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_{TABLE_NAME}_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED'))
);
```

### 6. 완료 보고
생성하거나 건너뛴 파일 목록을 출력한다.
- 새로 생성한 파일: ✅ 생성
- 이미 존재하여 건너뛴 경우: ⏭️ 스킵

다음 작업 안내도 함께 출력한다:
```
다음 단계:
1. {MODEL}.kt — TODO 주석 위치에 도메인 필드를 추가하세요.
2. {TABLE_NAME}.sql — 컬럼 정의를 완성하고 DB에 적용하세요.
3. 필요한 경우 /add-api {DOMAIN} 로 Controller를 생성하세요.
```
