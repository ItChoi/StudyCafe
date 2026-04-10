package org.example.studycafe.branch.presentation.request

import org.example.studycafe.branch.application.dto.BranchSearchCondition
import org.example.studycafe.common.enumeration.CommonStatus

data class BranchSearchRequest(
    val name: String? = null,
    val status: CommonStatus? = null,
) {
    fun toCondition() = BranchSearchCondition(
        name = name,
        status = status,
    )
}
