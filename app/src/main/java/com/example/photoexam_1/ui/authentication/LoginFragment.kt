package com.example.photoexam_1.ui.authentication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.photoexam_1.R
import com.example.photoexam_1.databinding.FragmentLoginBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.example.photoexam_1.ui.main.MainActivity
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnRegister?.setOnClickListener {
            val fragmentRegis = RegisFragment()
            val move = requireActivity().supportFragmentManager.beginTransaction()
            move.replace(R.id.frame_container, fragmentRegis)
            move.addToBackStack(null)
            move.commit()
        }

        val txtEmail = binding?.txtEmail
        val txtPassword = binding?.txtPassword

        binding?.btnLogin?.setOnClickListener {
            with(binding) {
                val email = txtEmail?.text.toString()
                val password = txtPassword?.text.toString()
                if (email.isEmpty() || password.isEmpty() || password.length != 6 || !isEmailValid(email)) {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle(R.string.login_failed)
                        setMessage(R.string.loginRegis_failed_msg)
                        setNegativeButton(R.string.back, null)
                        create()
                        show()
                    }
                } else { viewModel.login(email, password) }
            }
        }
        viewModel.loading.observe(viewLifecycleOwner){ showLoading(it) }
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            alertDialog()
        }
        viewModel.errorLogin.observe(viewLifecycleOwner) {
            it.let { errorResponse ->
                Toast.makeText(requireActivity(), errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding?.loginProgressBar?.visibility = View.VISIBLE
        } else {
            binding?.loginProgressBar?.visibility = View.INVISIBLE
        }
    }

    private fun alertDialog() {
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (isAdded && user.isLogin) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle(R.string.login)
                    setMessage(R.string.msgLogin)
                    setPositiveButton(R.string.next) { _, _ ->
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun isEmailValid(s: String): Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(s)
        return matcher.matches()
    }

}