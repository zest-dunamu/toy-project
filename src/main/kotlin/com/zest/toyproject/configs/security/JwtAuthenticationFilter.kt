package com.zest.toyproject.configs.security

import com.zest.toyproject.common.utils.LogUtil.Companion.log
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token: String? = jwtTokenProvider.resolveToken(request as HttpServletRequest)

        if (isValidateToken(token)) {
            val authentication: Authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
            log.info(authentication.name + " : authentication success")
        }

        chain.doFilter(request, response)
    }

    private fun isValidateToken(token: String?): Boolean {
        return token != null && jwtTokenProvider.isValidateToken(token)
    }
}
