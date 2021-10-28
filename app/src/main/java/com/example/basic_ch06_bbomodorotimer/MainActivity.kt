package com.example.basic_ch06_bbomodorotimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.basic_ch06_bbomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        bindView()

    }

    private fun bindView() {
        mainBinding.seekBar.setOnSeekBarChangeListener(

            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    mainBinding.remainMinutesTextView.text = "%02d".format(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )
    }

}


/*
"%02d"
% : 명령 시작을 의미
0 : 채워질 문자
2 : 총 자리수
d : 십진수로 된 정수

출처: https://fruitdev.tistory.com/177 [과일가게 개발자]



 */