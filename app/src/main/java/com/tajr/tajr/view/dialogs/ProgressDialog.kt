package com.tajr.tajr.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.tajr.tajr.R
import com.tajr.tajr.databinding.ProgressDialogBinding

class ProgressDialog :DialogFragment() {

    lateinit var binding:ProgressDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.progress_dialog,container,false)


        setCancelable(false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (dialog != null && dialog!!.window != null)
            dialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}