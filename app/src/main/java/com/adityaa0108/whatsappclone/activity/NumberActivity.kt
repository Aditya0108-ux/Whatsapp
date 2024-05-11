package com.adityaa0108.whatsappclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adityaa0108.whatsappclone.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.phoneNumber.requestFocus()
        auth = FirebaseAuth.getInstance()
        //if user is already loggedIn with phone Number
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.otpButton.setOnClickListener{
            if(binding.phoneNumber.text!!.isEmpty()){
                    Toast.makeText(applicationContext,"Please enter your number",Toast.LENGTH_SHORT).show()
            }
            else{
                var intent = Intent(this,OTPActivity::class.java)
                intent.putExtra("number",binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }
        }

    }
}