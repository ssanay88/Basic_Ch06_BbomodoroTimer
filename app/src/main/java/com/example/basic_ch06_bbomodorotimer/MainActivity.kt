package com.example.basic_ch06_bbomodorotimer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import com.example.basic_ch06_bbomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding     // 뷰바인딩

    // 카운트 다운 타이머가 작동하고 있는 경우 현재의 카운트다운타이머를 저장할 변수
    private var currentCountDownTimer : CountDownTimer? = null    // 카운트 다운 타이머 객체

    // 사운드풀 사용을 위한 선언
    private val soundPool = SoundPool.Builder().build()    // 사운드풀 객체 생성

    // 사운드 풀에서 로드된 Id를 담을 변수들
    private var tickingSoundId: Int? = null    // 똑딱 소리
    private var bellSoundId: Int? = null    // 알람 소리

    override fun onCreate(savedInstanceState: Bundle?) {

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        bindView()
        initSounds()

    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()    // 모든 스트림 재시작
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()   // 모든 스트림 종료
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()    // 앱 종료 시 메모리에서 soundPool 해제
    }


    private fun bindView() {
        // seekBar에 대한 이벤트 리스너
        mainBinding.seekBar.setOnSeekBarChangeListener(

            // SeekBar 변경 익명 객체
            object : SeekBar.OnSeekBarChangeListener {

                // 진행도가 변경되는 경우
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        updateRemainTime(progress * 60 * 1000L)    // 해당하는 시간으로 UI 업데이트
                    }
                }

                // SeekBar를 움직이기 시작하는 경우
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    stopCountDown()    // 카운트 다운을 멈춘다

                }

                // SeekBar를 조절하다가 멈출 때 카운트 다운 시작
                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    seekBar ?: return    // SeekBar가 null일 경우 그냥 탈출

                    if (mainBinding.seekBar.progress == 0) {
                        stopCountDown()    // 실행이 종료된 경우
                    } else {
                        startCountDown()    // 설정한 기준으로 카운트다운 시작
                    }

                }

            }
        )
    }

    // 사운드 관련해서 선언
    private fun initSounds() {
        tickingSoundId = soundPool.load(this , R.raw.timer_ticking , 1)    //
        bellSoundId = soundPool.load(this , R.raw.timer_bell , 1)
    }

    // 카운트 다운 타이머를 생성
    private fun createCountDownTimer(initialMillis:Long) =
        // CountDownTimer 생성 즉시 반환하는 코드 , 익명 객체
        object: CountDownTimer(initialMillis, 1000L) {

            // 줄어들때마다 실행
           override fun onTick(millisUntilFinished: Long) {

                // 매 1초마다 UI 갱신해야 함
                updateRemainTime(millisUntilFinished)    // 남은 시간에 대한 UI 업데이트
                updateSeekBar(millisUntilFinished)    // 남은 시간에 대한 SeekBar 업데이트

            }

            // 카운트 다운이 종료될 경우
            override fun onFinish() {

                completeCountDown()    // 카운트 다운 종료

            }
        }.start()    // CountDownTimer 실행


    // 남은 시간에 대한 UI 업데이트
    private fun updateRemainTime(remainMillis:Long) {

        val remainSeconds = remainMillis / 1000    // 밀리초 단위로 받아온 수를 초단위로 바꿔 준다

        mainBinding.remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)    // 분 단위
        mainBinding.remainSecondsTextView.text = "%02d".format(remainSeconds % 60)    // 초 단위
    }

    private fun updateSeekBar(remainMillis: Long) {

        mainBinding.seekBar.progress = (remainMillis / 1000 / 60).toInt()    // 진행도를 업데이트

    }

    // 타이머를 생성 및 시작
    private fun startCountDown() {
        // seekbar에서 설정한 분을 밀리초로 변환
        currentCountDownTimer = createCountDownTimer(mainBinding.seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()    // 카운트다운타이머가 null이 아닐 경우 시작

        // tickingSoundId가 null이 아닐 경우 let 호출
        tickingSoundId?.let { soundId ->
            soundPool.play(soundId , 1F , 1F , 0, -1 , 1F)
        }
    }

    // 타이머 종료
    private fun stopCountDown() {
        currentCountDownTimer?.cancel()    // 현재 진행하고 있는 카운트 다운이 null이 아닐경우 취소
        currentCountDownTimer = null    // 다시 초기화

        soundPool.autoPause()    // 소리도 정지

    }

    // 카운트 다운 종료 시 실행
    private fun completeCountDown() {

        updateRemainTime(0)
        updateSeekBar(0)

        // 째깍째깍 소리 종료 후 벨소리 출력
        soundPool.autoPause()
        bellSoundId?.let {  soundId ->
            soundPool.play(soundId , 1F , 1F , 0 , 0 , 1F)
        }
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