package org.example.studycafe.branch.application.dto

data class BranchCreateCommand(
    val name: String,
    val bizNumber: String,
    val contactNumber: String,
    val address: String,
    val detailAddress: String,
)
