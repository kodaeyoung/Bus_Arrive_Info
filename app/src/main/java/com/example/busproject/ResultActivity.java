package com.example.busproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    Intent intent;
    String routeId;
    String plateNo1;
    String locationNo1;
    String predictTime1;
    String name;
    String input;
    String response;
    Handler handler =new Handler();

    static List<PrintWriter>list =
            Collections.synchronizedList(new ArrayList<PrintWriter>());
    PrintWriter out =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        intent = getIntent();
        routeId= intent.getStringExtra("route");
        plateNo1=intent.getStringExtra("plate");
        locationNo1=intent.getStringExtra("location");
        predictTime1=intent.getStringExtra("predict");
        name=intent.getStringExtra("name");

        TextView t_name=findViewById(R.id.stopName);
        TextView t_routeId=findViewById(R.id.routeId);
        TextView t_plateNo1=findViewById(R.id.plateNo1);
        TextView t_predictTime1=findViewById(R.id.predictTime1);
        TextView t_locationNo1=findViewById(R.id.locationNo1);



        t_name.setText(name);
        t_routeId.setText(routeId);
        t_plateNo1.setText(plateNo1);
        t_predictTime1.setText(predictTime1);
        t_locationNo1.setText(locationNo1);

        Button socketButton=findViewById(R.id.socketButton);

        input=name.concat("/").concat(plateNo1);

        socketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketThread thread=new SocketThread(input);
                thread.start();
                Toast.makeText(ResultActivity.this,routeId+"번 차량 탑승 예약되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    class SocketThread extends Thread{

        String temp;
        String input; // 탑승위치/차량번호
        PrintWriter out =null;

        public SocketThread(String input){
            this.input = input;
        }


        @Override
        public void run() {



            try{
                int port = 9999; //포트 번호는 서버측과 똑같이
                Socket socket = new Socket("172.20.10.6" , port); // 소켓 열어주기
               // ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
              //  outstream.writeObject(input); // 출력 스트림에 데이터 넣기
             //   outstream.flush(); // 출력
                out = new PrintWriter(socket.getOutputStream());
                list.add(out);

                sendAll("user");
                sendAll(input);
                sendAll("quit");
                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void sendAll(String s) throws UnsupportedEncodingException {
        for(PrintWriter out: list) {
            out.println(s);
            out.flush();
        }
    }

}

