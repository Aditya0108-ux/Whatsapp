package com.adityaa0108.whatsappclone.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adityaa0108.whatsappclone.R
import com.adityaa0108.whatsappclone.adapter.MessageAdapter
import com.adityaa0108.whatsappclone.databinding.ActivityChatBinding
import com.adityaa0108.whatsappclone.databinding.SentItemLayoutBinding
import com.adityaa0108.whatsappclone.model.MessageModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date



class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sentItemLayoutBinding: SentItemLayoutBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var receiverUid:String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var list:ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        sentItemLayoutBinding = SentItemLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Change Status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        }

        binding.messageBox.requestFocus()
        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        val userName = intent.getStringExtra("name")
        binding.userName.text = userName
        val userImage = intent.getStringExtra("image")
        Glide.with(this).load(userImage).into(binding.userProfile)

        binding.backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.chatToolbar.setOnClickListener {
           Toast.makeText(applicationContext,"Toolbar is clicked",Toast.LENGTH_SHORT).show()
        }
        


        binding.send.setOnClickListener {
            if(binding.messageBox.text.isEmpty()){
                          Toast.makeText(this,"Please enter your message",Toast.LENGTH_SHORT).show()
            }
            else{
                    val message =
                        MessageModel(binding.messageBox.text.toString(),senderUid, Date().time)
                    val randomKey = database.reference.push().key
                    database.reference.child("chats")
                        .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                        .addOnSuccessListener {
                            database.reference.child("chats").child(receiverRoom).child("message")
                                .child(randomKey!!).setValue(message).addOnSuccessListener {
                                            binding.messageBox.text = null
                                           Toast.makeText(this,"Message Sent!!",Toast.LENGTH_SHORT).show()
                                }
                        }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    binding.userListRecyclerView.adapter = MessageAdapter(this@ChatActivity,list)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Error: $error",Toast.LENGTH_SHORT).show()
                }

            })



    }







}