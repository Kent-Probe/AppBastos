package com.kent.appbastos.usecases.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.usecases.mainPrincipal.MainMenu
import com.kent.appbastos.usecases.screenComplete.ScreenPrincipal

enum class ProviderType{
    GOOGLE
}

//Value for save
const val VALUES_SAVE = "com.kent.appbastos.profile"

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashThem)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //First Screen
        first()

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {

            //Configuration
            val googleConfig: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConfig)

            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }


        //Method of the share values
        session()
    }

    private fun mainMenuScreen(email:String, provider: ProviderType, profile:String){
        val intent = Intent(this, MainMenu::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            putExtra("profile", profile)
        }
        startActivity(intent)
    }

    private fun first(){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val isFirst = pref.getString("isFirst", null)
        if(isFirst == null){
            val intent = Intent(this, ScreenPrincipal::class.java)
            startActivity(intent)
            val prefFirst = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefFirst.putString("isFirst", "si")
            prefFirst.apply()
        }

    }

    private fun session(){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString("profile", null).toString()
        val email = pref.getString("email", null)
        val provider = pref.getString("provider", null)

        if(email != null && provider != null){
            mainMenuScreen(email, ProviderType.valueOf(provider), profile)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            mainMenuScreen(account.email ?: "", ProviderType.GOOGLE, account.givenName ?: "")
                        }else{
                            Alerts().showAlert(
                                "Error",
                                "No se a iniciado sesion",
                                "Aceptar",
                                this
                            )
                        }
                    }
                }
            }catch (e: ApiException){
                Alerts().showAlert(
                    "Error",
                    "No se a iniciado sesion",
                    "Aceptar",
                    this
                )
            }
        }
    }

}