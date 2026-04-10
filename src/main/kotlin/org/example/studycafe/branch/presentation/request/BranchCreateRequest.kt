package org.example.studycafe.branch.presentation.request

import jakarta.validation.constraints.NotBlank
import org.example.studycafe.branch.application.dto.BranchCreateCommand

data class BranchCreateRequest(
    @field:NotBlank(message = "지점명은 필수입니다.")
    val name: String,

    @field:NotBlank(message = "사업자등록번호는 필수입니다.")
    val bizNumber: String,

    @field:NotBlank(message = "연락처는 필수입니다.")
    val contactNumber: String,

    @field:NotBlank(message = "주소는 필수입니다.")
    val address: String,

    val detailAddress: String = "",
) {
    fun toCommand() = BranchCreateCommand(
        name = name,
        bizNumber = bizNumber,
        contactNumber = contactNumber,
        address = address,
        detailAddress = detailAddress,
    )
}
