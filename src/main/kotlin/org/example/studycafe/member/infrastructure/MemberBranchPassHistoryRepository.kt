package org.example.studycafe.member.infrastructure

import org.example.studycafe.member.domain.MemberBranchPassHistory
import org.springframework.data.jpa.repository.JpaRepository

interface MemberBranchPassHistoryRepository : JpaRepository<MemberBranchPassHistory, Long>
