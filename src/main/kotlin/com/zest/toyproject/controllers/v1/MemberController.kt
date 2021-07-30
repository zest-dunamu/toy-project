package com.zest.toyproject.controllers.v1

import com.zest.toyproject.models.Member
import com.zest.toyproject.models.request.SignUpMemberRequest
import com.zest.toyproject.models.request.SignInMemberRequest
import com.zest.toyproject.responses.MemberResponse
import com.zest.toyproject.services.MemberService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {

    @ApiOperation(value = "멤버 조회")
    @GetMapping("/{memberId}")
    fun getMember(@PathVariable memberId:Long) : MemberResponse {
        val member:Member = memberService.findById(memberId)

        return MemberResponse(
            id = member.id!!,
            username = member.username,
            nickname = member.nickname!!
        )
    }

    @ApiOperation(value = "회원가입")
    @PostMapping
    fun signUp(@Valid @RequestBody memberInfo:SignUpMemberRequest) : ResponseEntity<MemberResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(memberInfo))
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/signin")
    fun signIn(@Valid @RequestBody signInMemberRequest: SignInMemberRequest) : MemberResponse{
        return memberService.signIn(signInMemberRequest)
    }

}
