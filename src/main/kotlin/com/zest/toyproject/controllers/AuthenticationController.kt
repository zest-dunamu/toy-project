package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.dto.response.LoginResponse
import com.zest.toyproject.services.AuthenticationService
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
) {
    @ApiOperation(value = "로그인 + 인증")
    @PostMapping("/login")
    fun login(@Valid @RequestBody signInMemberRequest: SignInMemberRequest): LoginResponse {
        return authenticationService.login(signInMemberRequest.username, signInMemberRequest.password)
    }
}
