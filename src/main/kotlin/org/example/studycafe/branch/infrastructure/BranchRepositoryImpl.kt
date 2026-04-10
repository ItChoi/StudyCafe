package org.example.studycafe.branch.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.studycafe.branch.application.dto.BranchSearchCondition
import org.example.studycafe.branch.domain.Branch
import org.example.studycafe.branch.domain.QBranch

class BranchRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : BranchRepositoryCustom {

    override fun search(condition: BranchSearchCondition): List<Branch> {
        val branch = QBranch.branch

        return queryFactory
            .selectFrom(branch)
            .where(
                condition.name?.let { branch.name.containsIgnoreCase(it) },
                condition.status?.let { branch.status.eq(it) },
            )
            .fetch()
    }
}
