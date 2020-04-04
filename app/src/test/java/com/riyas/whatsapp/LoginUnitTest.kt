package com.riyas.whatsapp

import android.app.Application
import com.riyas.whatsapp.model.SignInErrors
import com.riyas.whatsapp.model.UserDetails

import com.riyas.whatsapp.view_model.UserViewModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

import org.mockito.Mockito.mock
import org.mockito.Mockito



@RunWith(MockitoJUnitRunner::class)
class LoginUnitTest {

    @Mock
    lateinit var mUserViewModel:UserViewModel
    @Mock
    lateinit var mApplication:Application
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

         mApplication = mock(Application::class.java)
        mUserViewModel = UserViewModel(mApplication)



        Mockito.`when`(mApplication.getString(R.string.invalid_password_sp))
            .thenReturn("Password should have at least 1 special character")
        Mockito.`when`(mApplication.getString(R.string.invalid_user_name))
            .thenReturn("Invalid email")
        Mockito.`when`(mApplication.getString(R.string.invalid_password_caps))
            .thenReturn("Password should have at least 1 uppercase")
        Mockito.`when`(mApplication.getString(R.string.invalid_password_lower))
            .thenReturn("Password should have at least 1 lowercase")
        Mockito.`when`(mApplication.getString(R.string.invalid_password_sp))
            .thenReturn("Password should have at least 1 special character")
        Mockito.`when`(mApplication.getString(R.string.invalid_password_length))
            .thenReturn("Password should be of 8-16 characters length")
        Mockito.`when`(mApplication.getString(R.string.invalid_password_number))
            .thenReturn("Password should have at least 1 Number")
    }

    @Test
    fun usernameValidator_CorrectUsernameReturnsTrue() {
        val username="name@email.com"

        val errorMsg=mUserViewModel.isValidUserName(username)

        assertNull(errorMsg)
    }
    @Test
    fun usernameValidator_CorrectUsernameWithSubDomainReturnsTrue() {
        val username="name@email.co.in"

        val errorMsg=mUserViewModel.isValidUserName(username)

        assertNull(errorMsg)
    }
    @Test
    fun usernameValidator_InvalidUsernameWithoutDotCom() {
        val username="name@email"

        val errorMsg=mUserViewModel.isValidUserName(username)

        assertNull(errorMsg)
    }



    @Test
    fun usernameValidator_InvalidUsernameWithSpecialCharacter() {
        val username="name@email.!com"
        
        val errorMsg=mUserViewModel.isValidUserName(username)

        assertNull(errorMsg)
    }

    @Test
    fun passwordValidator_InvalidWithOutUpperCase() {
        val password="qwe@123"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }
    @Test
    fun passwordValidator_InvalidWithOutSmallLetter() {
        val password="QWE@123"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }
    @Test
    fun passwordValidator_InvalidWithOutNumber() {
        val password="QWE@qwe"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }

    @Test
    fun passwordValidator_InvalidWithOutSpecialCharacter() {
        val password="QWE123qwe"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }
    @Test
    fun passwordValidator_InvalidLength1() {
        val password="Qwe@123"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }

    @Test
    fun passwordValidator_InvalidLength2() {
        val password="Qwe@123qdasfdgfsdgfsgsfgfs"


        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }

    @Test
    fun passwordValidator_validPassword() {
        val password="Worldofplay@2020"

        val errorMsg=mUserViewModel.isValidPassword(password)

        assertNull(errorMsg)

    }


}
