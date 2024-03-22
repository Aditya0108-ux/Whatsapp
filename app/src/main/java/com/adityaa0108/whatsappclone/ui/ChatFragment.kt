package com.adityaa0108.whatsappclone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.adityaa0108.whatsappclone.adapter.ChatAdapter
import com.adityaa0108.whatsappclone.databinding.FragmentChatBinding
import com.adityaa0108.whatsappclone.model.UserModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class   ChatFragment : Fragment() {

    lateinit var binding:FragmentChatBinding
    private lateinit var database:FirebaseDatabase
    lateinit var userList:ArrayList<UserModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance()
        userList = ArrayList()

        database.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   userList.clear()
                    for(snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModel::class.java)
                        if(user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
                    binding.userListRecyclerView.adapter = ChatAdapter(requireContext(),userList)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        // Inflate the layout for this fragment
         return binding.root



    }


}