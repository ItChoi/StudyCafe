package org.example.studycafe.member.application

import org.example.studycafe.common.security.JwtTokenProvider
import org.example.studycafe.member.application.dto.MemberLoginCommand
import org.example.studycafe.member.application.dto.MemberSignUpCommand
import org.example.studycafe.member.domain.Member
import org.example.studycafe.member.domain.MemberException
import org.example.studycafe.member.domain.MemberExceptionMessage
import org.example.studycafe.member.infrastructure.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @Transactional
    fun signUp(command: MemberSignUpCommand): Pair<String, Member> {
        if (memberRepository.existsByNickname(command.nickname)) {
            throw MemberException(MemberExceptionMessage.DUPLICATE_NICKNAME)
        }

        val member = Member(
            nickname = command.nickname,
            password = passwordEncoder.encode(command.password)!!,
            gender = command.gender,
        )
        val saved = memberRepository.save(member)
        val token = jwtTokenProvider.generateToken(requireNotNull(saved.id))
        return token to saved
    }

    fun login(command: MemberLoginCommand): Pair<String, Member> {
        val member = memberRepository.findByNickname(command.nickname)
            ?: throw MemberException(MemberExceptionMessage.INVALID_PASSWORD)

        if (!passwordEncoder.matches(command.password, member.password)) {
            throw MemberException(MemberExceptionMessage.INVALID_PASSWORD)
        }

        val token = jwtTokenProvider.generateToken(requireNotNull(member.id))
        return token to member
    }

    fun getById(memberId: Long): Member {
        return memberRepository.findById(memberId).orElseThrow {
            MemberException(MemberExceptionMessage.MEMBER_NOT_FOUND)
        }
    }
}
