package com.example.photoexam_1.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoexam_1.R
import com.example.photoexam_1.data.model.User
import com.example.photoexam_1.data.remote.response.DataItem
import com.example.photoexam_1.databinding.ActivityMainBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.example.photoexam_1.ui.authentication.AuthActivity
import com.example.photoexam_1.ui.upload.UploadActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser().observe(this) { user ->
            if(!user.isLogin) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            } else {
                val token = user.token
                viewModel.getAllPhoto(token)
                binding.toolbarMain.title = user.email
            }
        }
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        viewModel.loading.observe(this) { showLoading(it) }
        viewModel.photoSuccess.observe(this) { setRecyclerView(it.data) }

        binding.btnAdd.setOnClickListener { startActivity(Intent(this, UploadActivity::class.java)) }

        binding.toolbarMain.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.btnLogout -> {
                    logOutAlertDialog()
                    true
                } else -> false
            }
        }

    }

    private fun setRecyclerView(listPhoto: List<DataItem>){
        val adapter = MainAdapter()
        adapter.submitList(listPhoto)
        binding.rvMain.adapter = adapter

        if (listPhoto.isEmpty()){
            binding.imgEmpty.visibility = View.VISIBLE
            binding.txtNoData.visibility = View.VISIBLE
        } else {
            binding.imgEmpty.visibility = View.INVISIBLE
            binding.txtNoData.visibility = View.INVISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.MainProgressBar.visibility = View.VISIBLE
        } else {
            binding.MainProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun logOutAlertDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.logout)
            setMessage(R.string.msgLogout)
            setNegativeButton(R.string.no, null)
            setPositiveButton(R.string.yes) { _, _ ->
                viewModel.logOut()
            }
            show()
        }
    }
}