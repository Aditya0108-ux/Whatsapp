package com.adityaa0108.whatsappclone.activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adityaa0108.whatsappclone.adapter.ViewPagerAdapter
import com.adityaa0108.whatsappclone.databinding.ActivityMainBinding
import com.adityaa0108.whatsappclone.ui.CallFragment
import com.adityaa0108.whatsappclone.R
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



        binding!!.toolbar.inflateMenu(R.menu.menu)// binding is a ViewBinding


        // Get the custom icon drawable
        val customMenuIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_more_vert_24)
        // Set the custom overflow icon on the Toolbar
        binding!!.toolbar.overflowIcon = customMenuIcon

        // Inflate and set the custom menu
        binding!!.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    val intent = Intent(applicationContext,ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.logout -> {
                   auth.signOut()
                    val intent = Intent(applicationContext,NumberActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }





        //if user is not SignUp
        if(auth.currentUser == null)
        {
            startActivity(Intent(this, NumberActivity::class.java))
            finish()
        }

         val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
         binding!!.viewPager.adapter = adapter
         binding!!.include.setupWithViewPager(binding!!.viewPager)





    }





}
