package com.tajr.tajr.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tajr.tajr.models.AccessTokenRes
import com.tajr.tajr.models.RefreshTokenRes
import com.tajr.tajr.models.Response
import com.tajr.tajr.server.BaseClient
import retrofit2.Call
import retrofit2.Callback


object GsignInRepo {

    lateinit var result:MutableLiveData<AuthResult>
    lateinit var refreshTokenResponse: MutableLiveData<Response<RefreshTokenRes>>
    lateinit var accessTokenResponse: MutableLiveData<Response<AccessTokenRes>>

    private val REFRESH_TOKEN_URL="https://accounts.google.com/o/oauth2/token"
    private val grant_type="authorization_code"
    private  val TAG = "GsignInRepo"

    @JvmStatic
    public fun signIn(idToken: String?): MutableLiveData<AuthResult> {
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

    @JvmStatic
    public fun getRefreshToken(code :String,client_id:String,client_secret:String): MutableLiveData<Response<RefreshTokenRes>> {
        refreshTokenResponse=MutableLiveData()
        val res=Response<RefreshTokenRes>(null,true,null)

        val fields=HashMap<String,String>()
        fields["grant_type"]=grant_type
        //fields["access_type"]="offline"
        fields["code"]=code
        fields["client_id"]=client_id
        fields["client_secret"]=client_secret


        BaseClient
                .getApiService()
                .getRefreshToken(REFRESH_TOKEN_URL,fields)
                .enqueue(object : Callback<RefreshTokenRes>{
                    override fun onFailure(call: Call<RefreshTokenRes>, t: Throwable) {
                        res.loading=false
                        res.error=t.message
                        Log.d(TAG, "onFailure: "+t.message)
                    }

                    override fun onResponse(call: Call<RefreshTokenRes>, response: retrofit2.Response<RefreshTokenRes>) {
                        res.loading=false
                        if (response.isSuccessful&&response.body()!=null){
                            res.model=response.body()
                            res.error=null
                            refreshTokenResponse.value=res
                            Log.d(TAG, "onResponse: "+res.model?.refresh_token)

                        }else
                            Log.d(TAG, "onResponse: "+response.code())
                    }


                })
        return refreshTokenResponse
    }


    fun getAccessToken(refresh_token :String,client_id:String,client_secret:String){
        accessTokenResponse= MutableLiveData()

        val fields=HashMap<String,String>()
        fields["grant_type"]="refresh_token"
        fields["refresh_token"]=refresh_token
        fields["client_id"]=client_id
        fields["client_secret"]=client_secret
        BaseClient
                .getApiService()
                .getAccessToken(REFRESH_TOKEN_URL,fields)
                .enqueue(object :Callback<AccessTokenRes>{
                    override fun onResponse(call: Call<AccessTokenRes>, response: retrofit2.Response<AccessTokenRes>) {
                        val accessToken=response.body()
                        if (response.isSuccessful&&accessToken!=null){
                            accessTokenResponse.value= Response(accessToken,false,null)
                        }else
                            accessTokenResponse.value=Response(null,false,"error loading access token")

                    }

                    override fun onFailure(call: Call<AccessTokenRes>, t: Throwable) {
                        accessTokenResponse.value=Response(null,false,t.message)

                    }

                })
    }


}