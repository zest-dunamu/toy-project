package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.GrantRoleRequest
import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.dto.response.LoginResponse
import com.zest.toyproject.services.AuthenticationService
import com.zest.toyproject.services.MemberService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["인증"])
@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val memberService: MemberService,
) {
    @ApiOperation(value = "로그인 + 인증")
    @PostMapping("/login")
    fun login(@Valid @RequestBody signInMemberRequest: SignInMemberRequest): LoginResponse {
        return authenticationService.login(signInMemberRequest.username, signInMemberRequest.password)
    }

    @ApiOperation(value = "권한 부여")
    @PostMapping("/grant")
    fun grantRole(@Valid @RequestBody grantRoleRequest: GrantRoleRequest) {
        val member = memberService.findById(grantRoleRequest.memberId)
        return authenticationService.grantRole(member, grantRoleRequest.role)
    }
}
