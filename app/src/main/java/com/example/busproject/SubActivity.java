package com.example.busproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SubActivity extends AppCompatActivity {
    private Intent intent;
    final String TAG = "SubActivity";

    ArrayList<Busitem> list = null;
    Busitem bus = null;
    RecyclerView recyclerView;
    String routeId;
    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        intent = getIntent();
        routeId = intent.getStringExtra("id");
        name= intent.getStringExtra("name");

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {


        String location = URLEncoder.encode(routeId);

        String arrDataKey = "zYmgixu%2Fk4l6g7Yr3EbKZyY18Y%2FAm%2FVUpCbn803qZ9bejqTg0N1UBsgSiQkNkgOK1XUcTEcomhAGC%2FB1Q%2FhmWg%3D%3D";

        String arrRequestUrl;

        String newnum;


        @Override
        protected String doInBackground(String... strings) {


            arrRequestUrl="http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList?"//요청 URL
                    +"serviceKey="+arrDataKey
                    +"&stationId=" +location;



            try {
                boolean b_locationNo1 = false;
                boolean b_plateNo1 = false;
                boolean b_routeId = false;
                boolean b_predictTime1 = false;
                boolean b_lowPlate1 = false;

                URL url = new URL(arrRequestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Busitem>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("busArrivalList") && bus != null) {
                                list.add(bus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("busArrivalList")) {
                                bus = new Busitem();
                            }
                            if (parser.getName().equals("locationNo1")) b_locationNo1 = true;
                            if (parser.getName().equals("plateNo1")) b_plateNo1 = true;
                            if (parser.getName().equals("routeId")) b_routeId = true;
                            if (parser.getName().equals("predictTime1")) b_predictTime1 = true;
                            if (parser.getName().equals("lowPlate1")) b_lowPlate1 = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (b_lowPlate1) {
                                //bus.setLowPlate1(parser.getText());
                                if (parser.getText().equals("1")) {
                                    bus.setLowPlate1("저상버스");
                                }
                                else{
                                    bus.setLowPlate1("휠체어 탑승불가");
                                }
                                b_lowPlate1 = false;
                            }

                               else if (b_locationNo1) {
                                    bus.setLocationNo1(parser.getText());
                                    b_locationNo1 = false;
                                } else if (b_plateNo1) {
                                    bus.setPlateNo1(parser.getText());
                                    b_plateNo1 = false;
                                } else if (b_routeId) {
                                    newnum = busNum(parser.getText());
                                    bus.setRouteId(newnum);
                                    b_routeId = false;
                                } else if (b_predictTime1) {
                                    bus.setPredictTime1(parser.getText());
                                    b_predictTime1 = false;
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

        protected String busNum(String inner) throws IOException, ParserConfigurationException, SAXException {
            String out="-";
            String outer;
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=zYmgixu%2Fk4l6g7Yr3EbKZyY18Y%2FAm%2FVUpCbn803qZ9bejqTg0N1UBsgSiQkNkgOK1XUcTEcomhAGC%2FB1Q%2FhmWg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + URLEncoder.encode(inner, "UTF-8")); /*노선ID*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                String parsingUrl = urlBuilder.toString();
                Document doc = dBuilder.parse(parsingUrl);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("busRouteInfoItem");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    org.w3c.dom.Element eElement = (Element) nNode;
                    outer = getTagValue("routeName", (org.w3c.dom.Element) eElement);
                    return outer;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return out;
        }


        protected String getTagValue(String tag, org.w3c.dom.Element eElement) {
            //결과를 저장할 result 변수 선언
            String result = "";
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            if(nlList != null) {
                Node nItem = nlList.item(0);
                if(nItem != null)
                    result = nItem.getTextContent();
                //result = nlList.item(0).getTextContent();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            SubAdapter adapter = new SubAdapter( getApplicationContext(), list , name);
            recyclerView.setAdapter(adapter);
        }
    }
}