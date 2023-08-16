package com.example.busproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class Search extends AppCompatActivity {

    final String TAG = "Search";

    EditText edit;
    ArrayList<Item> list = null;
    Item bus = null;
    RecyclerView recyclerView;
    ImageButton mVoiceBtn;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        edit= (EditText)findViewById(R.id.edit);
        mVoiceBtn = findViewById(R.id.voiceBtn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
    }


    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "탑승할 정류장을 말하세요");
        if(intent.resolveActivity(getPackageManager())!=null) {
            try {

                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            } catch (Exception e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this,"마이크 사용불가, 정류장을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edit.setText(result.get(0));
                }
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                break;
            }
        }
    }

    public void mOnClick(View v){
        switch( v.getId() ){
            case R.id.button:

                //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
        }
    }
    public class MyAsyncTask extends AsyncTask<String, Void, String> {


        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);

        String idDataKey="zYmgixu%2Fk4l6g7Yr3EbKZyY18Y%2FAm%2FVUpCbn803qZ9bejqTg0N1UBsgSiQkNkgOK1XUcTEcomhAGC%2FB1Q%2FhmWg%3D%3D";

        String idrequestUrl;


        @Override
        protected String doInBackground(String... strings) {

            idrequestUrl="http://apis.data.go.kr/6410000/busstationservice/getBusStationList?"
                    +"serviceKey="+idDataKey
                    +"&keyword=" + location;




            try {
                boolean b_mobileNo = false;
                boolean b_stationName = false;
                boolean b_stationId = false;

                URL url = new URL(idrequestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("busStationList") && bus != null) {
                                list.add(bus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("busStationList")) {
                                bus = new Item();
                            }
                            if (parser.getName().equals("stationId")) b_stationId = true;
                            if (parser.getName().equals("mobileNo")) b_mobileNo = true;
                            if (parser.getName().equals("stationName")) b_stationName = true;

                            break;
                        case XmlPullParser.TEXT:
                            if (b_mobileNo) {
                                bus.setMobileNo(parser.getText());
                                b_mobileNo = false;
                            } else if (b_stationName) {
                                bus.setStationName(parser.getText());
                                b_stationName = false;
                            } else if(b_stationId){
                                bus.setStationId(parser.getText());
                                b_stationId = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            MyAdapter adapter = new MyAdapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }
}