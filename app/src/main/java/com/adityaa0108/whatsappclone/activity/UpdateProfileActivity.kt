package com.adityaa0108.whatsappclone.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adityaa0108.whatsappclone.LoadingAlert
import com.adityaa0108.whatsappclone.databinding.ActivityProfileBinding
import com.adityaa0108.whatsappclone.databinding.ActivityUpdateProfileBinding
import com.adityaa0108.whatsappclone.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date


class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg : Uri
    private lateinit var loadingAlert: LoadingAlert

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Updating Profile...").setCancelable(false)
        loadingAlert = LoadingAlert(this)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT //specify that we have to pick data from the device such as file or image
            intent.type = "image/*" //specify the type of content
            startActivityForResult(intent,1) //opens new activity
        }

        binding.continueButton.setOnClickListener {
            if(binding.userName.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter your name",Toast.LENGTH_SHORT).show()
            }else if(selectedImg == null){
                Toast.makeText(this,"Please select your Image",Toast.LENGTH_SHORT).show()
            }
            else {
                uploadData()
            }
        }
    }

    private fun uploadData() {
        loadingAlert.showLoadingDialog()
            val reference = storage.reference.child("Profile").child(auth.uid.toString()).child(Date().time.toString())
            reference.putFile(selectedImg).addOnCompleteListener{
                if(it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener {
                        task->
                        uploadInfo(task.toString())
                    }
                }
            }
    }

    private fun uploadInfo(imgUrl: String) {
               val user = UserModel(auth.uid.toString(),binding.userName.text.toString(),auth.currentUser?.phoneNumber.toString(),imgUrl)
                database.reference.child("users")
                    .child(auth.uid.toString())
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                       loadingAlert.dismissLoadingDialog()
                        finish()
                    }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            //data is an Intent containing the result data.
            if(data != null){
                if(data.data != null){ //(the selected image URI) is not null.
                    selectedImg = data.data!! //selects image URI and updates the user image
                    binding.userImage.setImageURI(selectedImg) //set the image uri
                }
            }
    }



}