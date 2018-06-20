package com.brobear.microphone;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageButton microphone;
    static boolean taking;
    VoiceService myService;
    String path;
    Button button;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        microphone = findViewById(R.id.imageButton);

        taking = false;
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taking) {//结束
                    microphone.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_talk));
                    taking = !taking;
                    String filePath = myService.stopRecording();
                    path = filePath;
                } else {//开始
                    microphone.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_talking));
                    taking = !taking;
                    myService = new VoiceService();
                    myService.startRecording();
                }
            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // path = "/storage/emulated/0/123.mp4";
                if(path==null)
                {
                    Log.d("player", "path==null");
                    return;
                }
                File file = new File(path);
                ///storage/emulated/0/123.mp4
                if (file.exists()) {
                    Log.d("player", "start");
                    startPlay(file);
                }else {
                    Log.d("player", "文件不存在："+path);
                }

            }
        });
    }

    private void startPlay(File mFile) {
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(mFile.getAbsolutePath());
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    // playEndOrFail(true);
                    Log.d("play", "end");
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    //  playEndOrFail(false);
                    Log.d("error", "播放失败");
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            // playEndOrFail(false);
        }

    }
}
