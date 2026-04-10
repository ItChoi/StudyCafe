package org.example.studycafe.branch.application

import org.example.studycafe.branch.application.dto.BranchCreateCommand
import org.example.studycafe.branch.application.dto.BranchSearchCondition
import org.example.studycafe.branch.domain.Branch
import org.example.studycafe.branch.infrastructure.BranchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Branch Bounded Context의 유스케이스를 담당하는 서비스.
 *
 * 규칙:
 * - 트랜잭션 경계는 이 레이어에서만 선언한다 (@Transactional).
 * - 비즈니스 로직은 Service에 직접 작성하지 않고 Domain Entity 메서드에 위임한다.
 * - 다른 Context의 Repository를 직접 주입받지 않는다.
 */
@Service
@Transactional(readOnly = true)
class BranchService(
    private val branchRepository: BranchRepository,
) {

    fun getBranch(id: Long): Branch {
        return branchRepository.findById(id)
            .orElseThrow { NoSuchElementException("지점을 찾을 수 없습니다. id=$id") }
    }

    fun searchBranches(condition: BranchSearchCondition): List<Branch> {
        // TODO: QueryDSL 조회 구현 후 branchRepository.search(condition) 호출
        TODO("구현 필요")
    }

    @Transactional
    fun createBranch(command: BranchCreateCommand): Branch {
        // TODO: 비즈니스 규칙 검증 후 저장
        TODO("구현 필요")
    }
}
