package org.example.studycafe.branch.application.dto

import org.example.studycafe.common.enumeration.CommonStatus

data class BranchSearchCondition(
    val name: String? = null,
    val status: CommonStatus? = null,
)
