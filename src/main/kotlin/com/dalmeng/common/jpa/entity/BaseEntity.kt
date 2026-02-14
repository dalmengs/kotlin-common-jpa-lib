package com.dalmeng.common.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import com.github.f4b6a3.ulid.UlidCreator

/**
 * JPA 엔티티의 기본 클래스
 * 
 * 모든 엔티티는 이 클래스를 상속받아 사용합니다.
 * 
 * 필드 설명:
 * - seq: 보안상 외부에는 공개하지 않는 내부 식별자. 정수 타입으로 조회 성능을 높이기 위해 사용됩니다.
 * - id: 공개되는 외부 식별자. ULID 형식의 문자열로 생성되며, 업데이트 불가능합니다.
 * - createdAt: 엔티티 생성 시각
 * - updatedAt: 엔티티 수정 시각
 * - isDeleted: Soft deletion을 위한 필드. 삭제된 엔티티는 true로 설정됩니다.
 */
@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    val seq: Long = 0L,

    @Column(name = "id", nullable = false, unique = true, updatable = false, length = 26)
    val id: String = generateId(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
) {
    companion object {
        private fun generateId(): String {
            return UlidCreator.getUlid().toString()
        }
    }
}
