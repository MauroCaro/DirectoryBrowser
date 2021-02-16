package com.base.core.mapper

import com.base.core.networking.login.LoginMapper
import com.dropbox.core.v2.common.RootInfo
import com.dropbox.core.v2.users.FullAccount
import com.dropbox.core.v2.users.Name
import com.dropbox.core.v2.userscommon.AccountType
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class LoginMapperTest {

    @Test
    fun `networkModel to domainModel successfully mapped`() {
        val userInformation = loginMapper.loginInformationNetworkModelToDomainModel(account, "123")

        assertNotNull(userInformation)
        userInformation.run {
            assertNotNull(token)
            assert(name == "test")
            assert(token == "123")
        }
    }

    private val loginMapper = LoginMapper()
    private lateinit var account: FullAccount

    @Before
    fun setup() {
        account = FullAccount(
            "0123456789012345678901234567890123456789",
            Name("test", "test", "test", "test", "test"),
            " test ",
            true,
            false,
            " CO ",
            "",
            true,
            AccountType.BASIC,
            RootInfo("test", "test")
        )
    }
}