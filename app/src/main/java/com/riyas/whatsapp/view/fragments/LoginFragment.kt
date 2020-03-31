package com.riyas.whatsapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.riyas.whatsapp.R
import com.riyas.whatsapp.databinding.FragmentLoginBinding
import com.riyas.whatsapp.model.AppConstants
import com.riyas.whatsapp.model.SignInErrors
import com.riyas.whatsapp.model.UserDetails
import com.riyas.whatsapp.view_model.UserViewModel

class LoginFragment : BaseFragment() {
    val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserViewModel?.mState.observe(this, Observer {
            when (it.state) {
                AppConstants.ACTION_LOGIN_SUCCESS -> {
                    findNavController().navigate(R.id.goToHomeFragment)
                }
                AppConstants.ACTION_LOGIN_FAILURE -> {
                    showDialog(it.msg, getString(android.R.string.ok))
                }
                AppConstants.NETWORK_FAILURE -> {
                    networkFailure()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        ) as FragmentLoginBinding


        binding.errorModel = SignInErrors(null)
        binding.viewModel = mUserViewModel
        binding.fragment = this
        binding.data = UserDetails(null, null)
        binding.lifecycleOwner = viewLifecycleOwner
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}