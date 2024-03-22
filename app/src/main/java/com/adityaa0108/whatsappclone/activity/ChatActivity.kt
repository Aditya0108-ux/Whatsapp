package com.adityaa0108.whatsappclone.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adityaa0108.whatsappclone.R
import com.adityaa0108.whatsappclone.adapter.MessageAdapter
import com.adityaa0108.whatsappclone.databinding.ActivityChatBinding
import com.adityaa0108.whatsappclone.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
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
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid



        database = FirebaseDatabase.getInstance()
        list = ArrayList()
        binding.send.setOnClickListener {
            if(binding.messageBox.text.isEmpty()){
                          Toast.makeText(this,"Please enter your message",Toast.LENGTH_SHORT).show()
            }
            else{
                    val message = MessageModel(binding.messageBox.text.toString(),senderUid, Date().time)
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

                    binding.recylerView.adapter = MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Error: $error",Toast.LENGTH_SHORT).show()
                }

            })

    }
}