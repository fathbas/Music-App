package com.fatihb.gfmusic.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

    }

    fun enterAccount(view: View) {

        auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
            .addOnCompleteListener { task->

                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"Hoşgeldiniz: ${auth.currentUser!!.displayName.toString()} bey",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MusicList::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }

    }

    fun goSignUp(view: View) {
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,IntroScreen::class.java)
        startActivity(intent)
        finish()
    }
}