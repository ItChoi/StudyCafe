package org.example.studycafe.member.presentation

import org.example.studycafe.member.application.MemberService
import org.example.studycafe.member.presentation.response.MemberResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/me")
    fun getMyInfo(@AuthenticationPrincipal memberId: Long): ResponseEntity<MemberResponse> {
        val member = memberService.getById(memberId)
        return ResponseEntity.ok(MemberResponse.from(member))
    }
}
