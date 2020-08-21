package com.fatihb.gfmusic.view

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fatihb.gfmusic.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.lang.Exception


class MusicPlayer : Fragment() {

    private lateinit var contentResolver: ContentResolver
    private lateinit var cursor: Cursor
    private lateinit var uri: Uri
    private  var position = 0
    private lateinit var py: MediaPlayer
    private lateinit var nameList: ArrayList<String>
    private lateinit var button: Button
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_player, container, false)
        button.isEnabled = false
        refreshLayout.isEnabled = false
        listView.isEnabled = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle =arguments

        musName.text = arguments?.getString("musicName").toString()
    }

    override fun onResume() {
        super.onResume()

        cursor.moveToPosition(position)
        py = MediaPlayer.create(this.activity, ContentUris.withAppendedId(uri,
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID))
                .toLong()
        ))

        var minute = py.duration/60000
        var second = (py.duration%1000)/100*10

        musicTime.text = minute.toString()+":"+second.toString()

        py.start()
        py.setOnCompletionListener {
            isFinish()
        }
        initSeekBar()

        playAndPause.setOnClickListener {
            if (py.isPlaying){
                playAndPause.setImageResource(R.drawable.ic_baseline_play_arrow)
                py.pause()
            }else{
                playAndPause.setImageResource(R.drawable.ic_baseline_pause_24)
                py.start()
                py.setOnCompletionListener {
                    isFinish()
                }
                initSeekBar()
            }
        }

        skipNext.setOnClickListener {
            isFinish()
            py.setOnCompletionListener {
                isFinish()
            }
        }

            skipPrev.setOnClickListener {
                py.stop()
                py.stop()
                py.reset()
                py.release()
                if (position - 1 == -1){
                    cursor.moveToFirst()
                    position = 0
                    musName.text = nameList[position]
                }else{
                    position-=1
                    cursor.moveToPosition(position)
                    musName.text = nameList[position]
                }
                py = MediaPlayer.create(this.activity, ContentUris.withAppendedId(uri,
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID))
                        .toLong()
                ))
                 minute = py.duration/60000
                 second = (py.duration%1000)/100*10

                musicTime.text = minute.toString()+":"+second.toString()

                py.start()
                py.setOnCompletionListener {
                    isFinish()
                }
                initSeekBar()
            }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    py.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        goBack.setOnClickListener {
            button.isEnabled = true
            refreshLayout.isEnabled = true
            listView.isEnabled = true
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            py.stop()
            py.reset()
            py.release()
        }
    }

    fun setData(contentResolver: ContentResolver, cursor: Cursor, uri: Uri,position: Int,nameList: ArrayList<String>,exit: Button,refreshLayout: SwipeRefreshLayout,listView: ListView){
            this.contentResolver = contentResolver
            this.cursor = cursor
            this.uri = uri
            this.position = position
            this.nameList = nameList
            this.button = exit
            this.refreshLayout = refreshLayout
            this.listView = listView
    }

    fun initSeekBar(){
        seekBar.max=py.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                try {
                    seekBar.progress = py.currentPosition
                    handler.postDelayed(this,1000)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        },0)
    }

    fun isFinish(){

            py.stop()
            py.reset()
            py.release()
            if (position + 1 == cursor.count){
                cursor.moveToFirst()
                position = 0
                musName.text = nameList[position]
            }else{
                position+=1
                cursor.moveToPosition(position)
                musName.text = nameList[position]
            }
            py = MediaPlayer.create(this.activity, ContentUris.withAppendedId(uri,
                cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID))
                    .toLong()
            ))
            var minute = py.duration/60000
            var second = (py.duration%1000)/100*10

            musicTime.text = minute.toString()+":"+second.toString()
            py.start()
            py.setOnCompletionListener {
                isFinish()
            }
            initSeekBar()
    }

}