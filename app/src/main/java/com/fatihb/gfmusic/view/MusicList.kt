package com.fatihb.gfmusic.view

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fatihb.gfmusic.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music_list.*

class MusicList : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var listOfSong : ArrayList<String>

    private lateinit var songCursor: Cursor

    private lateinit var songUri: Uri

    private lateinit var songResolver: ContentResolver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)



        auth = FirebaseAuth.getInstance()


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }else{
            doStuff()
        }
    }

    override fun onResume() {
        super.onResume()

        refreshLay.setOnRefreshListener {
            doStuff()
            refreshLay.isRefreshing = false
        }
    }

    private fun doStuff(){

        listOfSong = ArrayList()
        getMusic()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfSong)
        musicList.adapter = adapter

        musicList.setOnItemClickListener { parent, view, position, id ->

            val fragment = MusicPlayer()

            val bundle = Bundle()

            bundle.putString("musicName", listOfSong[position])

            fragment.arguments = bundle

            fragment.setData(songResolver, songCursor, songUri, position, listOfSong,exit,refreshLay,musicList)

            val fragmentManager = supportFragmentManager

            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.mediaPlayer, fragment).commit()


        }
    }



    private fun getMusic(){
        songResolver = contentResolver
        songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        songCursor = songResolver.query(songUri, null, null, null, null)!!

        if (songCursor != null && songCursor.moveToFirst()){
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)

            do {
                val currentTitle = songCursor.getString(songTitle)
                listOfSong.add(currentTitle)
            }while (songCursor.moveToNext())
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted!!", Toast.LENGTH_LONG).show()
                    doStuff()
                }
            } else{
                Toast.makeText(this, "Permission Not Granted!!", Toast.LENGTH_LONG).show()
            }
            return
        }
    }

    fun goIntroScreen(view: View) {
        auth.signOut()
        val intent = Intent(this, IntroScreen::class.java)
        startActivity(intent)
        finish()
    }


}