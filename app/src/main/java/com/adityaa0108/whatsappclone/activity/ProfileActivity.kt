package com.adityaa0108.whatsappclone.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adityaa0108.whatsappclone.LoadingAlert
import com.adityaa0108.whatsappclone.databinding.ActivityProfileBinding
import com.adityaa0108.whatsappclone.model.UserModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var loadingAlert: LoadingAlert


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.backButton.setOnClickListener {
            finish()
        }
        loadingAlert = LoadingAlert(this)


        val databaseReference = database.reference.child("users")
            .child(auth.uid.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                // Check if user.name is not null
                val userName: String? = user?.name
                // Convert the String? to Editable? using Editable.Factory
                val editableUserName: Editable? =
                    Editable.Factory.getInstance().newEditable(userName ?: "")
                // Set the Editable? value to the EditText's text property
                binding.userName.text = editableUserName

                val image = user?.imageUrl
                Glide.with(applicationContext)
                    .load(image)
                    .into(binding.userImage)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to fetch", Toast.LENGTH_SHORT).show()
            }

        })


        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT //specify that we have to pick data from the device such as file or image
            intent.type = "image/*" //specify the type of content
            startActivityForResult(intent,1) //opens new activity
        }

        binding.updateButton.setOnClickListener {
            if (binding.userName.text!!.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }else if(selectedImg == null){
                Toast.makeText(this, "Please select any image", Toast.LENGTH_SHORT).show()
            }
            else {
                  loadingAlert.showLoadingDialog()
                   uploadData()
            }
        }

    }
    private fun uploadData() {
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
        val currentUser = auth.currentUser
        currentUser.let { user ->
            val databaseReference = database.reference.child("users").child(user!!.uid)
            val updateUser = UserModel(auth.uid,binding.userName.text.toString(),auth.currentUser?.phoneNumber,imgUrl)
             databaseReference.setValue(updateUser)
                 .addOnCompleteListener { task->
                     if(task.isSuccessful){
                         loadingAlert.dismissLoadingDialog()
                         Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show()

                     }
                     else{
                         Toast.makeText(this,"Profile Failed to update",Toast.LENGTH_SHORT).show()
                     }
                 }


        }

    }


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