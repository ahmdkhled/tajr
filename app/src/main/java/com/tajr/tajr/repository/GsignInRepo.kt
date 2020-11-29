package com.tajr.tajr.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class GsignInRepo {

    lateinit var result:MutableLiveData<AuthResult>
    fun signIn(idToken: String?): MutableLiveData<AuthResult> {
        result=MutableLiveData()
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        result.value=it.result


                    }
                }
        return result
    }

}