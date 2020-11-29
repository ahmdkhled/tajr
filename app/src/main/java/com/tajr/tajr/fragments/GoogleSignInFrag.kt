package com.tajr.tajr.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tajr.tajr.R
import com.tajr.tajr.databinding.FragGoogleSigninBinding
import kotlinx.android.synthetic.main.layout_cities_spinner_item.view.*


class GoogleSignInFrag : Fragment() {

    lateinit var binding: FragGoogleSigninBinding
    private val G_SIGNIN_CODE:Int=22
    private  val TAG = "GoogleSignInFrag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_google_signin,container,false)
        binding.gSignin.setOnClickListener {
            signIn()
        }

        return binding.root
    }

    private fun signIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(Scope("https://www.googleapis.com/auth/spreadsheets"))
                .build()

        val gsc= GoogleSignIn.getClient(context!!,gso)

        startActivityForResult(gsc.signInIntent, G_SIGNIN_CODE)

    }



}