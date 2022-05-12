package com.example.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class WeatherActivity extends AppCompatActivity {
public final String TAG="WeatherActivity";

    StringBuilder sb;
    Thread th;
    double latitude;
    double longitude;
    String address;
    TextView tv_temperature,tv_humidity,tv_form,tv_hourMount,tv_windSpeed,tv_direction,tv_time;

    //String temperature,humidity,form,hourMount,windSpeed,direction;
    double temperature,humidity,form,hourMount,windSpeed,direction;

    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
        tv_temperature=findViewById(R.id.temperature);
        tv_humidity=findViewById(R.id.humidity);
        tv_form=findViewById(R.id.form);
        tv_hourMount=findViewById(R.id.hourMount);
        tv_windSpeed=findViewById(R.id.windSpeed);
        tv_direction=findViewById(R.id.direction);
        tv_time=findViewById(R.id.tv_time);



        latitude=getIntent().getDoubleExtra("latitude",0.0);
        longitude=getIntent().getDoubleExtra("longitude",0.0);
        address=getIntent().getStringExtra("address");


        int intLatitude= (int) latitude;
        int intLongitude= (int) longitude;

        Log.d(TAG,"latitude : "+ latitude);
        Log.d(TAG,"longitude : "+ longitude);

        // 날짜데이터 ex) 220505
        // 시간데이터 ex) 0100

        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        SimpleDateFormat date=new SimpleDateFormat("yyyyMMdd");
        System.out.println("date: "+date.format(timestamp));

        SimpleDateFormat date2=new SimpleDateFormat("HH00");
        System.out.println("date2 : "+date2.format(timestamp) );
        time=date2.format(timestamp);
        Log.e(TAG,"time : "+time);

        //20220505
        //1500

        SimpleDateFormat date3=new SimpleDateFormat("yyyy-MM-dd");

        th=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
                    // 첫번쨰
                    Log.e(TAG,"기준 time : "+time);
                    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=NqVyVZohzcQScgxJmsnYuYGfKMy6%2BtboV%2FTliRAzctaw%2B1UyJtrpUulqg4h1O%2BT66fngL5ICiDIwC6H%2BcNLTVg%3D%3D"); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
                    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("0900", "UTF-8")); /*한 페이지 결과 수*/
                    urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
                    urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date.format(timestamp), "UTF-8")); /*‘21년 6월 28일 발표*/
                    urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
                    //urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
                    urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(intLatitude+"", "UTF-8")); /*예보지점의 X 좌표값*/
                    urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(intLongitude+"", "UTF-8")); /*예보지점의 Y 좌표값*/
                    urlBuilder.append("&" + URLEncoder.encode("ftype","UTF-8") + "=" + URLEncoder.encode("SHRT", "UTF-8")); /*예보지점의 Y 좌표값*/
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());
                    BufferedReader rd;
                    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                    sb = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                    conn.disconnect();
                    //System.out.println(sb.toString());
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject object=new JSONObject(sb.toString());
            //System.out.println("object : "+object);
            JSONObject responseObject=new JSONObject(object.getString("response"));
            System.out.println("responseObject : "+responseObject);
            JSONObject headerObject=new JSONObject(responseObject.getString("header"));
            String resultMsg=headerObject.getString("resultMsg");
            System.out.println("resultMsg.toString() : "+resultMsg.toString());
            if(resultMsg.toString().equals("APPLICATION_ERROR")){
                // 두번째 에러만남
                Log.e(TAG,"APPLICATION_ERROR");
                // 정각~20분 에러남. -> 1시간 전 데이터 가져오기 위한 작업들
                int a= Integer.parseInt(date2.format(timestamp))-100;
                System.out.println("a : "+a);
                String str= String.valueOf(a);
                time=str;
                // 자릿수가 3자리이면 문자열 인덱스 '0' 에 문자열 0 추가
                if(str.length()<4){
                    StringBuffer sb = new StringBuffer();
                    sb.append(str);
                    sb.insert(0,"0"); //1500 -> 1400 ////1000-> 900 -> 0900
                    System.out.println("sb : "+sb);
                    time= String.valueOf(sb);
                    if(time.equals("00")){
                        time=time+"00";
                    }
                }
                // 다시 값 받아오기.
                th.interrupt();
                System.out.println("th.getState() : "+th.getState());
                th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
                            Log.e(TAG,"기준 time : "+time);
                            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=NqVyVZohzcQScgxJmsnYuYGfKMy6%2BtboV%2FTliRAzctaw%2B1UyJtrpUulqg4h1O%2BT66fngL5ICiDIwC6H%2BcNLTVg%3D%3D"); /*Service Key*/
                            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
                            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("0900", "UTF-8")); /*한 페이지 결과 수*/
                            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
                            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date.format(timestamp), "UTF-8")); /*‘21년 6월 28일 발표*/
                            //urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(date2.format(timestamp), "UTF-8")); /*06시 발표(정시단위) */
                            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
                            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(intLatitude+"", "UTF-8")); /*예보지점의 X 좌표값*/
                            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(intLongitude+"", "UTF-8")); /*예보지점의 Y 좌표값*/
                            urlBuilder.append("&" + URLEncoder.encode("ftype","UTF-8") + "=" + URLEncoder.encode("SHRT", "UTF-8")); /*예보지점의 Y 좌표값*/
                            URL url = new URL(urlBuilder.toString());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Content-type", "application/json");
                            System.out.println("Response code: " + conn.getResponseCode());
                            BufferedReader rd;
                            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            } else {
                                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                            }
                            sb = new StringBuilder();
                            String line;
                            while ((line = rd.readLine()) != null) {
                                sb.append(line);
                            }
                            rd.close();
                            conn.disconnect();
                            //System.out.println(sb.toString());
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
                th.join();
                //두번째 돌렸을때 값.
                object=new JSONObject(sb.toString());
                System.out.println("object : "+object);
                responseObject=new JSONObject(object.getString("response"));
                System.out.println("responseObject : "+responseObject);
                JSONObject body=new JSONObject(responseObject.getString("body"));
                JSONObject items=new JSONObject(body.getString("items"));
                System.out.println("items: "+items);
                JSONArray item=new JSONArray(items.getString("item"));
                System.out.println("item: "+item);

                for (int i = 0; i < item.length(); i++){
                    JSONObject json= (JSONObject) item.get(i);
                    String baseDate = (String) json.get("baseDate");
                    String baseTime = (String) json.get("baseTime");
                    String category = (String) json.get("category");

                    if(category.equals("PTY")){
                        // 강수형태
                        // 0 : 없음
                        // 1 : 비
                        // 2 : 비/눈
                        // 3 : 눈
                        // 4 : 소나기
                        category="강수형태";
                        String obsrValue= (String) json.get("obsrValue");
                        form= Double.parseDouble(obsrValue);
                        System.out.println("form : "+(int)form);
                        String text="";
                        switch((int)form){
                            case 0 :
                                text="없음";
                                tv_form.setText(text);
                                break;
                            case 1 :
                                text="비";
                                tv_form.setText(text);
                                break;
                            case 2 :text="비/눈";
                                tv_form.setText(text);
                                break;
                            case 3 :text="비";
                                tv_form.setText(text);
                                break;
                            case 4 :text="소나기";
                                tv_form.setText(text);
                                break;
                        }


                    }
                    if(category.equals("REH")){
                        category="습도";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_humidity.setText(obsrValue+"%");

                    }
                    if(category.equals("RN1")){
                        category="1시간 강수량";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_hourMount.setText(obsrValue+"/hour");

                    }
                    if(category.equals("T1H")){
                        category="기온";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_temperature.setText(obsrValue+"°C");

                    }
                    if(category.equals("UUU")){
                        category="동서바람성분";
                    }
                    if(category.equals("VEC")){
                        category="풍향";
                        String obsrValue= (String) json.get("obsrValue");
                        a= Integer.parseInt(obsrValue);
                        str="";
                        if(a<=45){
                            str="'남-남동' 풍";
                        }else if(a<=90){
                            str="'남동-동' 풍";
                        }else if(a<=135){
                            str="'남-남동' 풍";
                        }else if(a<=180){
                            str="'남-남동' 풍";
                        }else if(a<=180){
                            str="'남서-남' 풍";
                        }else if(a<=225){
                            str="'남-남서' 풍";
                        }else if(a<=270){
                            str="'남서-서' 풍";
                        }else if(a<=315){
                            str="'서-북서' 풍";
                        }else if(a<=360){
                            str="'북서-북' 풍";
                        }
                        tv_direction.setText(str);

                    }
                    if(category.equals("VVV")){
                        category="남북바람성분";
                    }
                    if(category.equals("WSD")){
                        category="풍속";
                        String obsrValue= (String) json.get("obsrValue");

                        a=(int)Float.parseFloat(obsrValue);
                        if(a<4){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 미세하거나 불지 않습니다.");
                        }
                        else if(a<9){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 약간 강합니다.");
                        }
                        else if(a<14){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 강합니다");
                        }
                        else if(a>14){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n강풍입니다.");
                        }

                    }


                    String nx = (String) String.valueOf(json.get("nx"));
                    String ny = (String) String.valueOf(json.get("ny"));
                    String obsrValue = (String) json.get("obsrValue");
                    //obsrValue : double타입
                    Double.parseDouble(obsrValue);

                    System.out.println("baseDate : "+baseDate);
                    System.out.println("baseTime : "+baseTime);
                    System.out.println("category : "+category);
                    System.out.println("nx : "+nx);
                    System.out.println("ny : "+ny);
                    System.out.println("obsrValue : "+ Double.parseDouble(obsrValue));




                }


            // 두번째 요청



            }else {
                JSONObject body=new JSONObject(responseObject.getString("body"));
                JSONObject items=new JSONObject(body.getString("items"));
                System.out.println("items: "+items);
                JSONArray item=new JSONArray(items.getString("item"));
                System.out.println("item: "+item);

                for (int i = 0; i < item.length(); i++){

                    JSONObject json= (JSONObject) item.get(i);
                    String baseDate = (String) json.get("baseDate");
                    String baseTime = (String) json.get("baseTime");
                    String category = (String) json.get("category");

                    if(category.equals("PTY")){
                        // 강수형태
                        // 0 : 없음
                        // 1 : 비
                        // 2 : 비/눈
                        // 3 : 눈
                        // 4 : 소나기
                        category="강수형태";
                        String obsrValue= (String) json.get("obsrValue");
                        form= Double.parseDouble(obsrValue);
                        System.out.println("form : "+(int)form);
                        String text="";
                        switch((int)form){
                            case 0 :
                                text="없음";
                                tv_form.setText(text);
                                break;
                            case 1 :
                                text="비";
                                tv_form.setText(text);
                                break;
                            case 2 :text="비/눈";
                                tv_form.setText(text);
                                break;
                            case 3 :text="비";
                                tv_form.setText(text);
                                break;
                            case 4 :text="소나기";
                                tv_form.setText(text);
                                break;
                        }


                    }
                    if(category.equals("REH")){
                        category="습도";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_humidity.setText(obsrValue+"%");

                    }
                    if(category.equals("RN1")){
                        category="1시간 강수량";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_hourMount.setText(obsrValue+"/hour");

                    }
                    if(category.equals("T1H")){
                        category="기온";
                        String obsrValue= (String) json.get("obsrValue");
                        tv_temperature.setText(obsrValue+"°C");

                    }
                    if(category.equals("UUU")){
                        category="동서바람성분";
                    }
                    if(category.equals("VEC")){
                        category="풍향";
                        String obsrValue= (String) json.get("obsrValue");
                        int a= Integer.parseInt(obsrValue);
                        String str="";
                        if(a<=45){
                            str="'남-남동' 풍";
                        }else if(a<=90){
                            str="'남동-동' 풍";
                        }else if(a<=135){
                            str="'남-남동' 풍";
                        }else if(a<=180){
                            str="'남-남동' 풍";
                        }else if(a<=180){
                            str="'남서-남' 풍";
                        }else if(a<=225){
                            str="'남-남서' 풍";
                        }else if(a<=270){
                            str="'남서-서' 풍";
                        }else if(a<=315){
                            str="'서-북서' 풍";
                        }else if(a<=360){
                            str="'북서-북' 풍";
                        }
                        tv_direction.setText(str);

                    }
                    if(category.equals("VVV")){
                        category="남북바람성분";
                    }
                    if(category.equals("WSD")){
                        category="풍속";
                        String obsrValue= (String) json.get("obsrValue");

                        int a=(int)Float.parseFloat(obsrValue);
                        if(a<4){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 미세하거나 불지 않습니다.");
                        }
                        else if(a<9){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 약간 강합니다.");
                        }
                        else if(a<14){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n바람이 강합니다");
                        }
                        else if(a>14){
                            tv_windSpeed.setText(obsrValue+"m/s"+"\n강풍입니다.");
                        }

                    }


                    String nx = (String) String.valueOf(json.get("nx"));
                    String ny = (String) String.valueOf(json.get("ny"));
                    String obsrValue = (String) json.get("obsrValue");
                    //obsrValue : double타입
                    Double.parseDouble(obsrValue);

                    System.out.println("baseDate : "+baseDate);
                    System.out.println("baseTime : "+baseTime);
                    System.out.println("category : "+category);
                    System.out.println("nx : "+nx);
                    System.out.println("ny : "+ny);
                    System.out.println("obsrValue : "+ Double.parseDouble(obsrValue));




                }
            }

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

        StringBuffer sb=new StringBuffer();
        sb.append(time);
        sb.insert(2,":");
        tv_time.setText(address+"\n"+date3.format(timestamp)+"  "+sb);

    }
}