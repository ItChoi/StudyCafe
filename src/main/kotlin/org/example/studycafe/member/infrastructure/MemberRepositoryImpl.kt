package org.example.studycafe.member.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory

/**
 * MemberRepositoryCustom의 QueryDSL 구현체.
 * 네이밍 규칙: {Repository 인터페이스명}Impl
 */
class MemberRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : MemberRepositoryCustom {
    // TODO: QueryDSL 조회 구현
}
