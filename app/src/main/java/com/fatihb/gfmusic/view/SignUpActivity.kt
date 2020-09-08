package com.fatihb.gfmusic.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference


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
                      user = auth.currentUser!!
                      database = FirebaseDatabase.getInstance()
                      ref = database.getReference("info/" + user.uid)
                      addData(name.text.toString(), surname.text.toString())
                      val intent = Intent()
                      this.setResult(1002, intent)
                      this.finish()
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
    private fun addData(name: String, surname: String){
        val map = mutableMapOf<String, String>()
        map["name"] = name
        map["surname"] = surname
        ref.setValue(map)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,IntroScreen::class.java)
        startActivity(intent)
        finish()
    }
}