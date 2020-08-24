package com.fatihb.gfmusic.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
    }

    fun sendInfo(view: View) {
         val email = mail.text.toString()
         val password = passw.text.toString()
         val nameAndSurname = name.text.toString()+ " " + surname.text.toString()
         if (email != null && password != null){

             auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                  if (task.isSuccessful){

                      val profileUpdates = userProfileChangeRequest {
                          displayName = nameAndSurname
                      }
                      auth.currentUser!!.updateProfile(profileUpdates)

                      auth.currentUser!!.sendEmailVerification().addOnCompleteListener {

                          if (it.isSuccessful){
                              Toast.makeText(applicationContext,"Kayıt başarılı! Lütfen e-postanızı aktive edin.",Toast.LENGTH_LONG).show()
                          }else{
                              Toast.makeText(applicationContext,it.exception.toString(),Toast.LENGTH_LONG).show()
                          }
                      }

                      val intent = Intent(this,LoginActivity::class.java)
                      startActivity(intent)
                  }
              }.addOnFailureListener { exception ->
                  if (exception != null){
                      Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                  }
              }
          }else{
              Toast.makeText(applicationContext,"E-postanız veya Şifreniz hatalı!",Toast.LENGTH_LONG).show()
          }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,IntroScreen::class.java)
        startActivity(intent)
        finish()
    }
}