package com.riyas.whatsapp.view_model

import android.app.Application
import com.riyas.whatsapp.R
import com.riyas.whatsapp.model.AppConstants
import com.riyas.whatsapp.model.SignInErrors
import com.riyas.whatsapp.model.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class UserViewModel(application: Application) : BaseViewModel(application) {

    val TAG = javaClass.simpleName

    fun doLogin(userDetails: UserDetails, siginError: SignInErrors) {

        siginError.userNameError = isValidUserName(userDetails.username)
        if (siginError.userNameError.isNullOrEmpty()

        ) {
            siginError.uiUpdate = true


        }else{
            siginError.uiUpdate = false
            setState(AppConstants.VALIDATION_ERROR, siginError.userPasswordError)
            return
        }
        siginError.userPasswordError = isValidPassword(userDetails.password)
        if ( siginError.userPasswordError.isNullOrEmpty()
        ) {
            siginError.uiUpdate = true


        }else{
            siginError.uiUpdate = false
            setState(AppConstants.VALIDATION_ERROR, siginError.userPasswordError)
            return
        }
        if (!checkConnection(getApplication())) {
            setState(AppConstants.NETWORK_FAILURE)
            return

        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = cloudDataManager?.login(userDetails)

            siginError.uiUpdate = false
            if (response?.error.isNullOrEmpty()) {
                setState(AppConstants.ACTION_LOGIN_SUCCESS)
            } else {
                setState(AppConstants.ACTION_LOGIN_FAILURE, response?.description)
            }
        }
    }

    fun onTextChanged(
        userDetails: UserDetails,
        text: CharSequence,
        siginError: SignInErrors,
        type: Int
    ) {
        when (type) {
            1 -> {
                userDetails.username = text?.toString()
            }
            2 -> {
                userDetails.password = text?.toString()
            }
        }
        siginError.disableState =
            userDetails.username.isNullOrEmpty() || userDetails.password.isNullOrEmpty()
    }

    fun isValidUserName(
        userName: String?
    ): String? {

        var errorMSg: String? = null
        if (userName.isNullOrEmpty()) {
            errorMSg = mApplication?.getString(R.string.invalid_user_name)
            return errorMSg;
        }

        if (!isValidEmail(userName)) {
            errorMSg = mApplication?.getString(R.string.invalid_user_name)
            return errorMSg;
        }
        return errorMSg;
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        );
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(
        password: String?
    ): String? {

        var errorMSg: String? = null
        if (password.isNullOrEmpty()) {
            errorMSg = mApplication?.getString(R.string.invalid_password)
            return errorMSg;
        }
        errorMSg = isValidPasswordFormat(password)
        return errorMSg;
    }

    private fun isValidPasswordFormat(
        password: String
    ): String? {

        val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val UpperCasePatten = Pattern.compile("[A-Z ]")
        val lowerCasePatten = Pattern.compile("[a-z ]")
        val digitCasePatten = Pattern.compile("[0-9 ]")

        var errorMSg: String? = null
        val length = password.length

        if (length < 4 && length > 15) {
            errorMSg = mApplication?.getString(R.string.invalid_password_length)
            return errorMSg
        }

        if (!UpperCasePatten.matcher(password).find()) {
            errorMSg = mApplication?.getString(R.string.invalid_password_caps)
            return errorMSg
        }
        if (!lowerCasePatten.matcher(password).find()) {
            errorMSg = mApplication?.getString(R.string.invalid_password_lower)
            return errorMSg
        }
        if (!digitCasePatten.matcher(password).find()) {
            errorMSg = mApplication?.getString(R.string.invalid_password_number)
            return errorMSg
        }
        if (!specailCharPatten.matcher(password).find()) {
            errorMSg = mApplication?.getString(R.string.invalid_password_sp)
            return errorMSg
        }

        return null

    }
}