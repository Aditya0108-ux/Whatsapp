package com.adityaa0108.whatsappclone.ui

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.adityaa0108.whatsappclone.databinding.DialogFbReactionsBinding


class FBReactionsDialog : DialogFragment(), View.OnClickListener {

    private var _binding: DialogFbReactionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFbReactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponents()
    }

    private fun initializeComponents() {
        with(binding) {
            binding.like.setOnClickListener(this@FBReactionsDialog)
            binding.love.setOnClickListener(this@FBReactionsDialog)
            binding.laugh.setOnClickListener(this@FBReactionsDialog)
            binding.sad.setOnClickListener(this@FBReactionsDialog)
            binding.wow.setOnClickListener(this@FBReactionsDialog)
            binding.angry.setOnClickListener(this@FBReactionsDialog)
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (binding.root) {
             binding.like -> {
                listener?.onReactionSelected(0)
                dialog?.dismiss()
            }
            binding.love -> {
                listener?.onReactionSelected(1)
                dialog?.dismiss()
            }
           binding.laugh -> {
                listener?.onReactionSelected(2)
                dialog?.dismiss()
            }
            binding.sad -> {
                listener?.onReactionSelected(3)
                dialog?.dismiss()
            }
            binding.wow -> {
                listener?.onReactionSelected(4)
                dialog?.dismiss()
            }
            binding.angry -> {
                listener?.onReactionSelected(5)
                dialog?.dismiss()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val manager = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            dimAmount = 0.0f
        }
        dialog.window?.apply {
            attributes = manager
            decorView.setBackgroundColor(resources.getColor(android.R.color.transparent))
        }
        dialog.setCanceledOnTouchOutside(true)
    }

    interface ReactionsListener {
        fun onReactionSelected(reactionType: Int)
    }

    private var listener: ReactionsListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as ReactionsListener
        } catch (exception: ClassCastException) {
            throw ClassCastException("$context must implement ReactionsListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}