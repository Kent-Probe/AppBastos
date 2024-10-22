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
import com.google.firebase.database.DataSnapshot
import com.kent.appbastos.R
import com.kent.appbastos.model.alerts.Alerts
import com.kent.appbastos.model.firebase.DataBaseShareData
import com.kent.appbastos.model.firebase.UserApp
import com.kent.appbastos.model.util.BasicEventCallback
import com.kent.appbastos.model.util.Keys
import com.kent.appbastos.usecases.mainPrincipal.MainMenu

enum class ProviderType{
    GOOGLE
}

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashThem)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //First Screen
        //first()

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

    private fun mainMenuScreen(email:String, provider: ProviderType, profile:String, rol: String){
        val intent = Intent(this, MainMenu::class.java).apply {
            putExtra(Keys.EMAIL, email)
            putExtra(Keys.PROVIDER_SESSION, provider.name)
            putExtra(Keys.PROFILE, profile)
            putExtra(Keys.ROL, rol)
        }
        startActivity(intent)
    }

    private fun session(){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val profile = pref.getString(Keys.PROFILE, null).toString()
        val email = pref.getString(Keys.EMAIL, null)
        val rol = pref.getString(Keys.ROL, null).toString()
        val provider = pref.getString(Keys.PROVIDER_SESSION, null)

        if(email != null && provider != null){
            mainMenuScreen(email, ProviderType.valueOf(provider), profile, rol)
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
                            val userCurrent = FirebaseAuth.getInstance().currentUser
                            val userApp = UserApp(account.displayName.toString(), account.familyName.toString(), account.email.toString(), Keys.ROL_USER)
                            DataBaseShareData().writeNewUserApp(this, userCurrent?.uid.toString(), userApp, object :
                                BasicEventCallback {
                                override fun onSuccess(dataSnapshot: DataSnapshot) {
                                    mainMenuScreen(account.email ?: "", ProviderType.GOOGLE, account.givenName ?: "", dataSnapshot.child(Keys.ROL).value.toString())
                                }

                                override fun onCancel() {
                                    TODO("Not yet implemented")
                                }

                                override fun databaseFailure() {
                                    TODO("Not yet implemented")
                                }

                            })
                        }else{
                            Alerts().showAlert(
                                title = Keys.ALERT_TITLE_ERROR,
                                message = Keys.ALERT_MSM,
                                positiveButton = Keys.ALERT_BUTTON,
                                this
                            )
                        }
                    }
                }
            }catch (e: ApiException){
                Alerts().showAlert(
                    title = Keys.ALERT_TITLE_ERROR,
                    message = Keys.ALERT_MSM,
                    positiveButton = Keys.ALERT_BUTTON,
                    this
                )
            }
        }
    }

}