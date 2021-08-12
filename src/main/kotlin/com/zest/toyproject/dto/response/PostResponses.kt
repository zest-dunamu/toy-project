package com.zest.toyproject.dto.response

import com.zest.toyproject.models.Post

data class PostResponses(
    val posts: List<PostResponse>
) {
    companion object {
        fun of(postList: List<Post>): PostResponses {
            val responses = mutableListOf<PostResponse>()

            postList.forEach {
                responses.add(PostResponse.of(it))
            }
            return PostResponses(responses)
        }
    }
}
