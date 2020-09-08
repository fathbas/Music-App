package com.fatihb.gfmusic.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.viewpager2.widget.ViewPager2
import com.fatihb.gfmusic.model.IntroSlide
import com.fatihb.gfmusic.R
import com.fatihb.gfmusic.adapter.IntroSliderAdapter
import kotlinx.android.synthetic.main.activity_intro_screen.*

class IntroScreen : AppCompatActivity() {

    private val sliderHandler = Handler()
    private var introString = arrayListOf<IntroSlide>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)

         introString = ArrayList<IntroSlide>()

        introString.add(IntroSlide(
            "ARAMIZA HOŞGELDİN",
            "G.F'ine göre özgürce müzik için kaydol."
        ))
        introString.add(IntroSlide(
            "ARA",
            "En geniş müzik arşivinden istediğin şarkıyı veya sanatçıyı dinle"
        ))
        introString.add(IntroSlide(
            "KALİTELİ MÜZİĞE ULAŞ",
            "Yüksek kalitede müziğin ritmini hisset."
        ))



        introSlider.adapter = IntroSliderAdapter(introString,introSlider)

        worm_dots_indicator.setViewPager2(introSlider)

        introSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable,3000)
            }
        })
    }

    private val sliderRunnable = Runnable {
        if (introSlider.currentItem == introString.size - 1){
            introSlider.currentItem = 0
        }else{
            introSlider.currentItem = introSlider.currentItem + 1
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 24 && resultCode == 1001){
            val intent = Intent(this,MusicList::class.java)
            startActivity(intent)
            finish()
        }
        if(requestCode == 24 && resultCode == 1002){
            val intent = Intent(this,LoginActivity::class.java)
            intent.putExtra("name", data?.getStringExtra("name"))
            intent.putExtra("surname", data?.getStringExtra("surname"))
            startActivityForResult(intent,24)
        }

    }

    fun enterLogin(view: View){
        val intent = Intent(this,LoginActivity::class.java)
        startActivityForResult(intent, 24)
    }

    fun enterSignUp(view: View){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivityForResult(intent, 24)
    }
}