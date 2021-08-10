package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.services.MemberService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["멤버"])
@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService,
) {

    @ApiOperation(value = "멤버 조회")
    @GetMapping("/{memberId}")
    fun getMember(@PathVariable memberId: Long): MemberResponse {
        return memberService.findById(memberId).let {
            MemberResponse(
                id = it.id!!,
                username = it.username,
                nickname = it.nickname!!
            )
        }
    }

    @ApiOperation(value = "회원가입")
    @PostMapping
    fun signUp(@Valid @RequestBody memberInfo: SignUpMemberRequest): ResponseEntity<MemberResponse> {
        val body = memberService.signUp(memberInfo).let {
            MemberResponse(
                id = it.id!!,
                username = it.username,
                nickname = it.nickname!!
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }
}
