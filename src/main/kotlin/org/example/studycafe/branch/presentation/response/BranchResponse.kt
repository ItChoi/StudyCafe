package org.example.studycafe.branch.presentation.response

import org.example.studycafe.branch.domain.Branch
import org.example.studycafe.common.enumeration.CommonStatus

/**
 * 지점 응답 DTO.
 * - Domain 객체(Branch)를 직접 반환하지 않고 이 DTO로 변환하여 반환한다.
 * - from() companion 함수로 Domain → DTO 변환을 캡슐화한다.
 */
data class BranchResponse(
    val id: Long,
    val name: String,
    val bizNumber: String,
    val contactNumber: String,
    val address: String,
    val detailAddress: String,
    val status: CommonStatus,
) {
    companion object {
        fun from(branch: Branch) = BranchResponse(
            id = requireNotNull(branch.id),
            name = requireNotNull(branch.name),
            bizNumber = requireNotNull(branch.bizNumber),
            contactNumber = requireNotNull(branch.contactNumber),
            address = requireNotNull(branch.address),
            detailAddress = requireNotNull(branch.detailAddress),
            status = branch.status,
        )
    }
}
