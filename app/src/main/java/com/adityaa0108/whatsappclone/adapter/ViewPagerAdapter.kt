package com.adityaa0108.whatsappclone.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    private val context:Context,
    fe: FragmentManager?,
    val list:ArrayList<Fragment>
) :  FragmentPagerAdapter(fe!!){
    override fun getCount(): Int {
        //return number of fragments
        return 3;
    }

    override fun getItem(position: Int): Fragment {
        //return the fragment from the list
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return the title of the page
        return TAB_TITLES[position]
    }

    companion object{
        val TAB_TITLES = arrayOf("Chats","Status","Call")
    }



}