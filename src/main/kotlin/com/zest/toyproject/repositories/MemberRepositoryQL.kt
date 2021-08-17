package com.zest.toyproject.repositories

import com.zest.toyproject.models.Member

interface MemberRepositoryQL {
    fun findByNickname(nickname: String): Member?
}