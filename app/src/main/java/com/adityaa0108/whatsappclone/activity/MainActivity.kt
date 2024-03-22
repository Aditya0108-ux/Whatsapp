package com.adityaa0108.whatsappclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.adityaa0108.whatsappclone.adapter.ViewPagerAdapter
import com.adityaa0108.whatsappclone.databinding.ActivityMainBinding
import com.adityaa0108.whatsappclone.ui.CallFragment
import com.adityaa0108.whatsappclone.ui.ChatFragment
import com.adityaa0108.whatsappclone.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null
   private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
          val fragmentArrayList = ArrayList<Fragment>()
          fragmentArrayList.add(ChatFragment())
          fragmentArrayList.add(StatusFragment())
          fragmentArrayList.add(CallFragment())

        //if user is not SignUp
        if(auth.currentUser == null){
            startActivity(Intent(this, NumberActivity::class.java))
            finish()
        }

         val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
         binding!!.viewPager.adapter = adapter
         binding!!.tabs.setupWithViewPager(binding!!.viewPager)





    }
}