package com.example.firebasetodoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firebasetodoapp.databinding.ActivityLoginBinding
import com.example.firebasetodoapp.fragments.LoginFragment
import com.example.firebasetodoapp.fragments.RegisterFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment to LoginFragment
        replaceFragment(LoginFragment())

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> {
                    replaceFragment(LoginFragment())
                    true
                }
                R.id.nav_register -> {
                    replaceFragment(RegisterFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}