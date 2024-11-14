package com.example.firebasetodoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        auth = FirebaseAuth.getInstance()
        Log.d("FirebaseInit", "Firebase initialized: ${FirebaseApp.getApps(this).isNotEmpty()}")

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not signed in, go to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // Close this activity
    }
}