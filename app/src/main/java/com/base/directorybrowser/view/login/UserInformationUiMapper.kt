package com.base.directorybrowser.view.login

import com.base.core.model.UserInformationModel

interface IUserInformationUiMapper {
    fun domainModelToUiModel(userInformationModel: UserInformationModel): UserInformationUiModel
}

class UserInformationUiMapper : IUserInformationUiMapper {
    override fun domainModelToUiModel(userInformationModel: UserInformationModel): UserInformationUiModel {
        return UserInformationUiModel(
            token = userInformationModel.token,
            name = userInformationModel.name,
            email = userInformationModel.email,
            profilePhoto = userInformationModel.profilePhoto
        )
    }
}