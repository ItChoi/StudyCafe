package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.Branch
import org.springframework.data.jpa.repository.JpaRepository

/**
 * BranchRepositoryCustom을 함께 상속하면
 * Spring Data JPA가 BranchRepositoryImpl을 자동으로 연결한다.
 */
interface BranchRepository : JpaRepository<Branch, Long>, BranchRepositoryCustom
