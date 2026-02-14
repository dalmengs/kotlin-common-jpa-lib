# Kotlin Common JPA Library

JPA 엔티티를 위한 공통 베이스 클래스를 제공하는 Kotlin 라이브러리입니다.

## 기능

- **ULID 기반 공개 식별자**: 외부에 공개되는 안전한 식별자 제공
- **내부 시퀀스**: 보안과 성능을 위한 내부 정수 식별자
- **Soft Deletion**: 논리적 삭제를 위한 `isDeleted` 필드
- **자동 타임스탬프**: 생성 시각과 수정 시각 자동 관리

## 필드 설명

### `seq: Long`
- **역할**: 보안상 외부에는 공개하지 않는 내부 식별자
- **타입**: 정수 (Long)
- **용도**: 데이터베이스 조회 성능을 높이기 위해 사용됩니다. 외부 API에는 노출하지 않습니다.

### `id: String`
- **역할**: 공개되는 외부 식별자
- **타입**: ULID 형식의 문자열 (26자)
- **용도**: API 응답 등 외부에 공개되는 식별자로 사용됩니다. 생성 후 업데이트 불가능합니다.

### `createdAt: LocalDateTime`
- **역할**: 엔티티 생성 시각
- **타입**: LocalDateTime
- **용도**: 엔티티가 생성된 시각을 자동으로 기록합니다.

### `updatedAt: LocalDateTime`
- **역할**: 엔티티 수정 시각
- **타입**: LocalDateTime
- **용도**: 엔티티가 수정될 때마다 자동으로 업데이트됩니다.

### `isDeleted: Boolean`
- **역할**: Soft deletion을 위한 플래그
- **타입**: Boolean
- **용도**: 논리적 삭제를 구현하기 위해 사용됩니다. `true`로 설정하면 삭제된 것으로 간주합니다.

## 설치

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.dalmeng:kotlin-common-jpa-lib:1.0.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'com.dalmeng:kotlin-common-jpa-lib:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.dalmeng</groupId>
    <artifactId>kotlin-common-jpa-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 사용법

### 기본 사용

엔티티 클래스에서 `BaseEntity`를 상속받아 사용합니다:

```kotlin
package com.dalmeng.problem.problem.entity

import com.dalmeng.common.jpa.entity.BaseEntity
import com.dalmeng.problem.tag.entity.Tag
import jakarta.persistence.*

@Entity
@Table(name = "problem_tag")
class ProblemTag(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    var tag: Tag,
) : BaseEntity() {

    companion object {
        fun create(
            problem: Problem,
            tag: Tag,
        ): ProblemTag {
            return ProblemTag(
                problem = problem,
                tag = tag,
            )
        }
    }
}
```

### Soft Deletion 사용

```kotlin
// 삭제 처리
problemTag.isDeleted = true
entityManager.merge(problemTag)

// 삭제되지 않은 엔티티만 조회
val activeTags = entityManager.createQuery(
    "SELECT pt FROM ProblemTag pt WHERE pt.isDeleted = false",
    ProblemTag::class.java
).resultList
```

### ID 사용

```kotlin
// 공개 식별자로 조회 (외부 API에서 사용)
val problemTag = entityManager.createQuery(
    "SELECT pt FROM ProblemTag pt WHERE pt.id = :id AND pt.isDeleted = false",
    ProblemTag::class.java
).setParameter("id", publicId).singleResult

// 내부 시퀀스로 조회 (내부 로직에서만 사용, 성능 최적화)
val problemTag = entityManager.find(ProblemTag::class.java, seq)
```

## 요구사항

- Java 17 이상
- Kotlin 1.9.22 이상
- Jakarta Persistence API 3.1.0 이상

## 라이선스

Apache License 2.0

## 저장소

https://github.com/dalmengs/kotlin-common-jpa-lib
