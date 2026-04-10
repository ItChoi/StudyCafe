package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.application.dto.BranchSearchCondition
import org.example.studycafe.branch.domain.Branch

interface BranchRepositoryCustom {
    fun search(condition: BranchSearchCondition): List<Branch>
}
