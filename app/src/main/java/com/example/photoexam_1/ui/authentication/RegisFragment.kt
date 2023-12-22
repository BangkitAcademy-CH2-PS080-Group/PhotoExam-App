package com.example.photoexam_1.ui.authentication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.photoexam_1.R
import com.example.photoexam_1.databinding.FragmentRegisBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

class RegisFragment : Fragment() {

    private lateinit var binding: FragmentRegisBinding
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtEmail = binding.txtEmailRegis
        val txtPassword = binding.txtPasswordRegis
        val txtPasswordConf = binding.txtPasswordConf

        binding.btnRegister.setOnClickListener {
            with(binding) {
                val email = txtEmail.text.toString()
                val password = txtPassword.text.toString()
                val passwordConf = txtPasswordConf.text.toString()
                if (password == passwordConf && isEmailValid(email)) {
                    viewModel.register(email, password)
                }
                else if (password != passwordConf) {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle(R.string.regis_failed)
                        setMessage(R.string.password_confirmation)
                        setNegativeButton(R.string.back, null)
                        create()
                        show()
                    }
                } else {
                    AlertDialog.Builder(requireActivity()).apply {
                        setTitle(R.string.regis_failed)
                        setMessage(R.string.loginRegis_failed_msg)
                        setNegativeButton(R.string.back, null)
                        create()
                        show()
                    }
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            moveToLogin()
        }

        viewModel.loading.observe(viewLifecycleOwner) { showLoading(it) }
        viewModel.regisSuccess.observe(viewLifecycleOwner) { alertDialog() }
        viewModel.errorRegis.observe(viewLifecycleOwner) {
            it.let { errorResponse ->
                Toast.makeText(requireActivity(), errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.regisProgressBar.visibility = View.VISIBLE
        } else {
            binding.regisProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun alertDialog(){
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.regisSuccess)
            setMessage(R.string.msgRegis)
            setPositiveButton(R.string.next) { _, _ ->
                val intent = Intent(context, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            create()
            show()
        }
    }

    private fun isEmailValid(s: String): Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(s)
        return matcher.matches()
    }

    private fun moveToLogin() {
        val fragmentLogin = LoginFragment()
        val move = requireActivity().supportFragmentManager.beginTransaction()
        move.replace(R.id.frame_container, fragmentLogin)
        move.addToBackStack(null)
        move.commit()
    }

}