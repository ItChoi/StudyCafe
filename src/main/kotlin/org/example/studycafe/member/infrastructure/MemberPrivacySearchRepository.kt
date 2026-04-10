package org.example.studycafe.member.infrastructure

import org.example.studycafe.member.domain.MemberPrivacySearch
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPrivacySearchRepository : JpaRepository<MemberPrivacySearch, Long>
