package org.example.studycafe.repository.member

import org.example.studycafe.domain.member.MemberPrivacySearch
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPrivacySearchRepository : JpaRepository<MemberPrivacySearch, Long>
