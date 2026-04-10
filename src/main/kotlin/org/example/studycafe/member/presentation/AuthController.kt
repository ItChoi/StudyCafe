package org.example.studycafe.member.presentation

import jakarta.validation.Valid
import org.example.studycafe.member.application.MemberService
import org.example.studycafe.member.presentation.request.MemberLoginRequest
import org.example.studycafe.member.presentation.request.MemberSignUpRequest
import org.example.studycafe.member.presentation.response.AuthResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val memberService: MemberService,
) {
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: MemberSignUpRequest): ResponseEntity<AuthResponse> {
        val (token, member) = memberService.signUp(request.toCommand())
        return ResponseEntity.ok(AuthResponse.of(token, member))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<AuthResponse> {
        val (token, member) = memberService.login(request.toCommand())
        return ResponseEntity.ok(AuthResponse.of(token, member))
    }
}
