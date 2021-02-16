package com.base.core.networking.login

import com.base.core.model.UserInformationModel
import com.base.core.util.Messages
import com.dropbox.core.v2.users.FullAccount
import com.dropbox.core.v2.users.Name

interface ILoginMapper {
    @Throws(IllegalStateException::class)
    fun loginInformationNetworkModelToDomainModel(
        accountInformation: FullAccount,
        token: String
    ): UserInformationModel
}

open class LoginMapper : ILoginMapper {
    override fun loginInformationNetworkModelToDomainModel(
        accountInformation: FullAccount,
        token: String
    ): UserInformationModel {
        return UserInformationModel(
            token = getTokenUser(token),
            countryCode = accountInformation.country,
            referralLink = accountInformation.referralLink,
            email = accountInformation.email,
            name = getFullName(accountInformation.name),
            accountId = accountInformation.accountId,
            profilePhoto = accountInformation.profilePhotoUrl
        )
    }

    private fun getTokenUser(token: String?): String {
        if (!token.isNullOrEmpty()) {
            return token
        }
        throw IllegalStateException(
            String.format(Messages.LOGIN_DATA_USER_PARSING_DATA_ERROR.message, "User token found NULL or empty string")
        )
    }

    private fun getFullName(name: Name?): String {
        name?.let {
            if (!it.displayName.isNullOrEmpty()) {
                return it.displayName
            }
            throw IllegalStateException(
                String.format(Messages.LOGIN_DATA_USER_PARSING_DATA_ERROR.message, "User name found NULL or empty string")
            )
        }?: kotlin.run {
            throw IllegalStateException(
                String.format(Messages.LOGIN_DATA_USER_PARSING_DATA_ERROR.message, "User name found NULL or empty string")
            )
        }
    }
}