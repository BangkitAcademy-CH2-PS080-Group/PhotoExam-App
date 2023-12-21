package com.example.photoexam_1.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.photoexam_1.R
import com.example.photoexam_1.databinding.ActivityAuthBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.example.photoexam_1.ui.main.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser().observe(this) {
            if(it.isLogin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val fragment = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)
        if(fragment !is LoginFragment){
            fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, loginFragment, LoginFragment::class.java.simpleName)
                .commit()
        }

    }
}