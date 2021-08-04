package com.zest.toyproject.configs.security

import com.zest.toyproject.services.MemberService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val memberService: MemberService
) {
    //JWT 토큰 생성
    fun createJwtToken(username: String?, roles: List<String?>): String {
        val claims = Jwts.claims().setSubject(username) //JWT payload 에 저장되는 정보
        claims["roles"] = roles
        val now = Date()
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + jwtProperties.expirationTime))
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    //JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String?): Authentication {
        val userDetails: UserDetails = memberService.loadUserByUsername(getUsernameFromToken(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 토큰에서 회원 정보 추출
    fun getUsernameFromToken(token: String?): String {
        return Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token).body.subject
    }

    //Request Header 에서 token 값을 가져옴 "JWT" : "TOKEN 값"
    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("JWT")
    }

    // 토큰의 유효성 + 만료일자 확인
    fun isValidateToken(token: String?): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token)
            isNotExpired(claims)
        } catch (e: Exception) {
            false
        }
    }

    fun isNotExpired(claims: Jws<Claims>): Boolean = !claims.body.expiration.before(Date())
}
