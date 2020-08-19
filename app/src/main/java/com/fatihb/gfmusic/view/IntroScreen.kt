package com.fatihb.gfmusic.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import com.fatihb.gfmusic.model.IntroSlide
import com.fatihb.gfmusic.adapter.IntroSliderAdapter
import com.fatihb.gfmusic.R
import kotlinx.android.synthetic.main.activity_intro_screen.*

class IntroScreen : AppCompatActivity() {

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "ARAMIZA HOŞGELDİN",
                "G.F'ine göre özgürce müzik için kaydol."
            ),
            IntroSlide(
                "ARA",
                "En geniş müzik arşivinden istediğin şarkıyı veya sanatçıyı dinle"
            ),
            IntroSlide(
                "KALİTELİ MÜZİĞE ULAŞ",
                "Yüksek kalitede müziğin ritmini hisset."
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)

        introSlider.adapter = introSliderAdapter
    }

    override fun onResume() {
        super.onResume()

        //Create variable for video
        val simpleVideoView: VideoView?
        //determine videovire id
        simpleVideoView = findViewById<View>(R.id.video_view) as VideoView

        //give video's path
        simpleVideoView.setVideoURI(
            Uri.parse("android.resource://"
                    + packageName + "/" + R.raw.output
            ))
        //simpleVideoView!!.requestFocus()

        //started video
        simpleVideoView.start()

        //when vide complete this method start again video
        simpleVideoView.setOnCompletionListener {
            simpleVideoView.start()
        }
    }

    fun enterLogin(view: View){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun enterSignUp(view: View){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}