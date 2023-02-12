package com.example.jhpl_instagram.model

class Post {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    var id: String? = null
    var content: String? = null
    var nLikes: Int? = null
    var likes: List<String>? = null
    var author: UserModel? = null
    var hasLiked: Boolean? = null
    var hasFollowed: Boolean? = null
    var postCategory: Int? = null
}