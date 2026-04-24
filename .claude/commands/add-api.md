# 새 API 엔드포인트 생성

인자: `$ARGUMENTS` (형식: `{도메인명} {HTTP메서드} {서브경로}`)
- 예: `seat GET /{id}`
- 예: `seat POST`
- 예: `seat DELETE /{id}`

## 처리 순서

### 1. 인자 파싱
`$ARGUMENTS`를 공백으로 분리한다.
- `DOMAIN` = 첫 번째 인자 (소문자, 예: seat)
- `DOMAIN_CLASS` = 첫 글자를 대문자로 변환 (예: Seat)
- `HTTP_METHOD` = 두 번째 인자 (대문자, 예: POST)
- `SUB_PATH` = 세 번째 인자 (없으면 빈 문자열)
- `DOMAIN_PLURAL` = 도메인명 복수형 (예: seats) — 영어 복수형 규칙 적용

### 2. 디렉토리 생성
아래 경로가 없으면 생성한다.
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/`
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/application/`
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/`
- `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/response/`
- POST / PUT / PATCH 인 경우에만: `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/request/`

### 3. Service 클래스 생성
`src/main/kotlin/org/example/studycafe/{DOMAIN}/application/{DOMAIN_CLASS}Service.kt` 가 없을 때만 생성한다.

```kotlin
package org.example.studycafe.{DOMAIN}.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class {DOMAIN_CLASS}Service {

}
```

### 4. Controller 클래스 생성 또는 메서드 추가
`src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/{DOMAIN_CLASS}Controller.kt` 파일을 확인한다.

**없으면** 아래 형식으로 새로 생성 후 메서드를 추가한다:

```kotlin
package org.example.studycafe.{DOMAIN}.presentation

import org.example.studycafe.{DOMAIN}.application.{DOMAIN_CLASS}Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/{DOMAIN_PLURAL}")
class {DOMAIN_CLASS}Controller(
    private val {DOMAIN}Service: {DOMAIN_CLASS}Service,
) {

}
```

**있으면** 기존 파일의 클래스 닫는 `}` 앞에 메서드를 추가한다.

### 5. HTTP 메서드별 메서드 코드 생성

아래 규칙에 따라 Controller에 추가할 메서드를 결정한다.

**GET (서브경로 없음)**:
```kotlin
    @GetMapping
    fun get{DOMAIN_CLASS}List(): ResponseEntity<List<{DOMAIN_CLASS}Response>> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `GetMapping`, `{DOMAIN_CLASS}Response`

**GET (서브경로 있음, /{id} 형태)**:
```kotlin
    @GetMapping("{SUB_PATH}")
    fun get{DOMAIN_CLASS}(@PathVariable id: Long): ResponseEntity<{DOMAIN_CLASS}Response> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `GetMapping`, `PathVariable`, `{DOMAIN_CLASS}Response`

**POST**:
```kotlin
    @PostMapping
    fun create{DOMAIN_CLASS}(@Valid @RequestBody request: {DOMAIN_CLASS}CreateRequest): ResponseEntity<{DOMAIN_CLASS}Response> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `PostMapping`, `Valid`, `RequestBody`, `{DOMAIN_CLASS}CreateRequest`, `{DOMAIN_CLASS}Response`

**PUT**:
```kotlin
    @PutMapping("{SUB_PATH}")
    fun update{DOMAIN_CLASS}(@PathVariable id: Long, @Valid @RequestBody request: {DOMAIN_CLASS}UpdateRequest): ResponseEntity<{DOMAIN_CLASS}Response> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `PutMapping`, `PathVariable`, `Valid`, `RequestBody`, `{DOMAIN_CLASS}UpdateRequest`, `{DOMAIN_CLASS}Response`

**PATCH**:
```kotlin
    @PatchMapping("{SUB_PATH}")
    fun patch{DOMAIN_CLASS}(@PathVariable id: Long, @Valid @RequestBody request: {DOMAIN_CLASS}UpdateRequest): ResponseEntity<{DOMAIN_CLASS}Response> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `PatchMapping`, `PathVariable`, `Valid`, `RequestBody`, `{DOMAIN_CLASS}UpdateRequest`, `{DOMAIN_CLASS}Response`

**DELETE**:
```kotlin
    @DeleteMapping("{SUB_PATH}")
    fun delete{DOMAIN_CLASS}(@PathVariable id: Long): ResponseEntity<Void> {
        // TODO: 구현
        TODO()
    }
```
필요한 import: `DeleteMapping`, `PathVariable`

Controller 파일의 import 블록에 누락된 import를 추가한다.

### 6. DTO 파일 생성 (없을 때만)

**Response DTO** — GET / POST / PUT / PATCH 인 경우:
경로: `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/response/{DOMAIN_CLASS}Response.kt`

```kotlin
package org.example.studycafe.{DOMAIN}.presentation.response

data class {DOMAIN_CLASS}Response(
    // TODO: 필드 추가
) {
    companion object {
        fun from({DOMAIN}: Any /* TODO: {DOMAIN_CLASS} 타입으로 변경 */): {DOMAIN_CLASS}Response {
            TODO()
        }
    }
}
```

**Create Request DTO** — POST 인 경우:
경로: `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/request/{DOMAIN_CLASS}CreateRequest.kt`

```kotlin
package org.example.studycafe.{DOMAIN}.presentation.request

data class {DOMAIN_CLASS}CreateRequest(
    // TODO: 필드 추가
) {
    fun toCommand(): Any /* TODO: Command 타입으로 변경 */ {
        TODO()
    }
}
```

**Update Request DTO** — PUT / PATCH 인 경우:
경로: `src/main/kotlin/org/example/studycafe/{DOMAIN}/presentation/request/{DOMAIN_CLASS}UpdateRequest.kt`

```kotlin
package org.example.studycafe.{DOMAIN}.presentation.request

data class {DOMAIN_CLASS}UpdateRequest(
    // TODO: 필드 추가
) {
    fun toCommand(): Any /* TODO: Command 타입으로 변경 */ {
        TODO()
    }
}
```

### 7. 완료 보고
생성하거나 수정한 파일 목록을 출력한다.
- 새로 생성한 파일: ✅ 생성
- 기존 파일에 추가한 경우: ✏️ 수정
- 이미 존재하여 건너뛴 경우: ⏭️ 스킵
