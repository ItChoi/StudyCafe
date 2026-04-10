package org.example.studycafe.branch.presentation

import org.example.studycafe.branch.application.BranchService
import org.example.studycafe.branch.presentation.request.BranchCreateRequest
import org.example.studycafe.branch.presentation.request.BranchSearchRequest
import org.example.studycafe.branch.presentation.response.BranchResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid

/**
 * 규칙:
 * - Service만 주입받는다. Repository를 직접 주입받지 않는다.
 * - Domain 객체를 그대로 반환하지 않고 Response DTO로 변환한다.
 * - 입력 검증은 @Valid + Request DTO에서 처리한다.
 */
@RestController
@RequestMapping("/api/v1/branches")
class BranchController(
    private val branchService: BranchService,
) {

    @GetMapping("/{id}")
    fun getBranch(@PathVariable id: Long): ResponseEntity<BranchResponse> {
        val branch = branchService.getBranch(id)
        return ResponseEntity.ok(BranchResponse.from(branch))
    }

    @GetMapping
    fun searchBranches(request: BranchSearchRequest): ResponseEntity<List<BranchResponse>> {
        val branches = branchService.searchBranches(request.toCondition())
        return ResponseEntity.ok(branches.map { BranchResponse.from(it) })
    }

    @PostMapping
    fun createBranch(@RequestBody @Valid request: BranchCreateRequest): ResponseEntity<BranchResponse> {
        val branch = branchService.createBranch(request.toCommand())
        return ResponseEntity.ok(BranchResponse.from(branch))
    }
}
