package com.fatihb.gfmusic.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        //Create variable for when later user can see login screen
        val background = object : Thread(){
            override fun run() {
                super.run()
                try {
                    sleep(2000)

                    if (currentUser != null){
                        val intent = Intent(this@SplashScreen,MusicList::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(this@SplashScreen,
                            IntroScreen::class.java)
                        startActivity(intent)
                        finish()
                    }


                }catch (e : Exception){
                    e.printStackTrace()
                }

            }
        }
        background.start()
    }
}