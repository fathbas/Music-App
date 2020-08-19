package com.fatihb.gfmusic.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth

class MusicList : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)

        auth = FirebaseAuth.getInstance()
    }

    fun goIntroScreen(view: View) {
        auth.signOut()
        val intent = Intent(this,IntroScreen::class.java)
        startActivity(intent)
        finish()
    }


}