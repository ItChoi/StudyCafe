package org.example.studycafe.repository.member

import org.example.studycafe.domain.member.MemberBranchPassHistory
import org.springframework.data.jpa.repository.JpaRepository

interface MemberBranchPassHistoryRepository : JpaRepository<MemberBranchPassHistory, Long>
