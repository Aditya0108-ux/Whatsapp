package com.adityaa0108.whatsappclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.adityaa0108.whatsappclone.R
import com.adityaa0108.whatsappclone.ReactionsListener
import com.adityaa0108.whatsappclone.activity.ChatActivity
import com.adityaa0108.whatsappclone.databinding.ReceiverItemLayoutBinding
import com.adityaa0108.whatsappclone.databinding.SentItemLayoutBinding
import com.adityaa0108.whatsappclone.model.MessageModel
import com.adityaa0108.whatsappclone.ui.FBReactionsDialog

import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context, var list:ArrayList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),ReactionsListener  {

    var ITEM_SENT = 1
    var ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return if(viewType == ITEM_SENT)
               SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        else   ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false))


    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun getItemCount(): Int {
       return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
                  val message = list[pos]

        if(holder.itemViewType == ITEM_SENT){
                    val viewHolder = holder as SentViewHolder
                    viewHolder.binding.userMessage.text = message.message
            viewHolder.binding.sentItem.setOnClickListener {
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
                getReactionsDialog()
            }
        }
        else{
                    val viewHolder = holder as ReceiverViewHolder
                    viewHolder.binding.userMessage.text = message.message
                }
    }


    private fun getReactionsDialog(): DialogFragment {
        val fbReactionsDialog = FBReactionsDialog()
        fbReactionsDialog.show((context as ChatActivity).supportFragmentManager, fbReactionsDialog::class.simpleName)
        return fbReactionsDialog
    }




    inner class SentViewHolder(view: View) : ViewHolder(view){
                    var binding = SentItemLayoutBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View) : ViewHolder(view){
                   var binding = ReceiverItemLayoutBinding.bind(view)
    }
    override fun onReactionSelected(reactionType: Int) {

        val viewHolder = SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout, null))
        when (reactionType) {
            0 -> {
                viewHolder.binding.feeling.visibility = View.VISIBLE
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_like)
            }
            1 -> {
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_love)
                viewHolder.binding.feeling.visibility = View.VISIBLE
            }
            2 -> {
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_laugh)
                viewHolder.binding.feeling.visibility = View.VISIBLE
            }
            3 -> {
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_sad)
                viewHolder.binding.feeling.visibility = View.VISIBLE
            }
            4 -> {
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_wow)
                viewHolder.binding.feeling.visibility = View.VISIBLE
            }
            5 -> {
                viewHolder.binding.feeling.setImageResource(R.drawable.ic_fb_angry)
                viewHolder.binding.feeling.visibility = View.VISIBLE
            }
        }
    }




}