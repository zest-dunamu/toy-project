package com.zest.toyproject.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AuthenticatedMember(
    var memberId: Long,
    username: String?,
    password: String?,
    authorities: Collection<GrantedAuthority>?
) : User(username, password, authorities)