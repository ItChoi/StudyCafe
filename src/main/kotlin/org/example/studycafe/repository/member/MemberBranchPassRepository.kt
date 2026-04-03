package org.example.studycafe.repository.member

import org.example.studycafe.domain.member.MemberBranchPass
import org.springframework.data.jpa.repository.JpaRepository

interface MemberBranchPassRepository : JpaRepository<MemberBranchPass, Long>
