package com.adityaa0108.whatsappclone.activity


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adityaa0108.whatsappclone.LoadingAlert
import com.adityaa0108.whatsappclone.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOtpactivityBinding
     private lateinit var auth:FirebaseAuth
     private lateinit var verificationId: String
     private lateinit var loadingAlert: LoadingAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please Wait...")
        builder.setMessage("Loading")
        builder.setCancelable(false)
        loadingAlert = LoadingAlert(this)
        loadingAlert.showLoadingDialog()


        val phoneNumber = "+91" + intent.getStringExtra("number")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                   loadingAlert.dismissLoadingDialog()
                    Log.e("Aditya","Error",p0)
                    Toast.makeText(this@OTPActivity,"Please try again ${p0}",Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    Toast.makeText(this@OTPActivity,"OTP sent",Toast.LENGTH_SHORT).show()
                   loadingAlert.dismissLoadingDialog()
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    binding.otpField.requestFocus()
                    binding.otpField.addTextChangedListener(object:TextWatcher{
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                        }

                    })
                    verificationId = p0
                }

            }).build()

          PhoneAuthProvider.verifyPhoneNumber(options)
        binding.otpField.setOtpCompletionListener {
            Log.d("Actual Value", it)
        }
           binding.Continue.setOnClickListener {
               if(binding.otpField.text!!.isEmpty()){
                   Toast.makeText(this,"Please enter the OTP",Toast.LENGTH_SHORT).show()
               }
               else{
                   loadingAlert.showLoadingDialog()
                   val credential = PhoneAuthProvider.getCredential(verificationId,binding.otpField.text!!.toString())
                   auth.signInWithCredential(credential)
                       .addOnCompleteListener {
                           if(it.isSuccessful){
                               loadingAlert.dismissLoadingDialog()
                                 startActivity(Intent(this,UpdateProfileActivity::class.java))
                               finish()
                           }else{
                               loadingAlert.dismissLoadingDialog()
                                 Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                           }
                       }
               }
           }

    }
}