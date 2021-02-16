package com.base.core.model

class UserInformationModel(
    val token: String,
    val countryCode: String?,
    val referralLink: String?,
    val accountId: String,
    val email: String,
    val name: String,
    val profilePhoto: String?
)