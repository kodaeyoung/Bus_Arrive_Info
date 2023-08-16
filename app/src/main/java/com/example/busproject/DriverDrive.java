package com.example.busproject;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class DriverDrive extends AppCompatActivity {

    TextToSpeech tts;
    String response;
    Handler handler =new Handler();
    BufferedReader in = null;
    String busStation;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_drive);

        TextView textView = findViewById(R.id.busNum);
        Button button =findViewById(R.id.button);

        Intent intent=getIntent();
        String busNum=intent.getExtras().getString("busNum");
        textView.setText(busNum);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);

                }
            }
       });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverDrive.SocketThread thread=new DriverDrive.SocketThread(busNum);
                thread.start();
                Toast.makeText(DriverDrive.this, "주행이 시작되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    class SocketThread extends Thread{
        String busNum;
        public SocketThread(String busNum) {
            this.busNum=busNum;        }

        @Override
        public void run() {

            try{
                int port = 9999; //포트 번호는 서버측과 똑같이
                Socket socket = new Socket("172.20.10.6" , port); // 소켓 열어주기

                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(in!=null) {
                    String inputMsg = in.readLine();
                    /* 토스트로 서버측 응답 결과 띄워줄 러너블 객체 생성하여 메인스레드 핸들러로 전달 */
                    if (inputMsg.contains(busNum)) {
                        busStation=inputMsg.split("/")[0];
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DriverDrive.this, busStation+"정류장에 휠체어 이용자가 있습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                         tts.speak(busStation+"정류장에 휠체어 이용자가 있습니다",TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}