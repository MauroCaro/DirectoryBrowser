package com.base.directorybrowser.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.directorybrowser.R
import com.base.directorybrowser.base.BaseActivity
import com.base.directorybrowser.dagger.injector.Injector
import com.base.directorybrowser.util.Data
import com.base.directorybrowser.util.Status
import com.base.directorybrowser.view.directory.DirectoryActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Injector.component().inject(this)
        loginViewModel = ViewModelProvider(this, vmFactory).get(LoginViewModel::class.java)
        initModel()
        login_activity_login_button?.setOnClickListener {
            loginViewModel.login()
        }
    }

    override fun onResume() {
        super.onResume()
        loginViewModel.fetchInformation()
    }

    private fun initModel() {
        loginViewModel.getUserInformationLiveData().observe(this, Observer(this::onUserInformationChanged))
    }

    private fun onUserInformationChanged(userInformation: Data<UserInformationUiModel>) {
        when (userInformation.responseType) {
            Status.LOADING -> {
                login_activity_login_button?.text = ""
                login_activity_lottie_button_loading?.visibility = View.VISIBLE
            }
            Status.SUCCESSFUL -> {
                this.userInformation = userInformation.data
                val intent = Intent(this, DirectoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            Status.ERROR -> {
                login_activity_login_button?.text = getString(R.string.login_activity_button_text)
                login_activity_lottie_button_loading?.visibility = View.GONE
            }
        }
    }
}