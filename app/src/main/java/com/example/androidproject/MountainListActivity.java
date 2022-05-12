package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MountainListActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG="MountainListActivity";
    // Get API
    private URL url;
    private HttpURLConnection conn;
    private StringBuilder sb;
    private JSONObject jsonObject=null;
    private BufferedReader rd=null;
    private StringBuilder urlBuilder;
    XmlPullParser parser;
    int eventType;
    //---------------------------------
    private MountainVO mountainVO;
    private MountainAdapter mAdapter;
    private ArrayList<MountainVO> mArrayList;
    //------------------------------------------------------------------
    private String mntName;
    private String mntSubName;
    private String mntHeight;
    private String mntLocation;
    private String mntCourse;
    private String mntPickReason;
    private String mntDetail;
    private String mntOverview;
    private String mntTransport;
    private String mntTourInfo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_list);
        Log.e(TAG,"onCreate");

        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_mnt_list);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList=new ArrayList<>();
        mAdapter=new MountainAdapter(mArrayList,this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        urlBuilder = new StringBuilder("http://openapi.forest.go.kr/openapi/service/cultureInfoService/gdTrailInfoOpenAPI"); /*URL*/
        Thread th=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=NqVyVZohzcQScgxJmsnYuYGfKMy6%2BtboV%2FTliRAzctaw%2B1UyJtrpUulqg4h1O%2BT66fngL5ICiDIwC6H%2BcNLTVg%3D%3D"); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("searchMtNm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*산명*/
                    urlBuilder.append("&" + URLEncoder.encode("searchArNm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*지역명*/
                    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("700", "UTF-8")); /*페이지당 표시 항목 수*/
                    url = new URL(urlBuilder.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    Log.e(TAG,"Response code: " + conn.getResponseCode());
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
                    Log.e(TAG,"sb.toString() : "+sb.toString());
                    rd.close();
                    conn.disconnect();

                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // item start -> end 반복
        // 원하는 태그 값이 if ( .equals) { 값 저장 }
        // 아니면 스킵
        // 닫힌 Item 태그가 나오면, 그때 객체생성.
        String text = null;
        String tagName=null;
        parser= Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(new StringReader(sb.toString()));
            eventType=parser.getEventType();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        Log.e(TAG,"get API Data");
        // xml 파일의 끝까지 반복
        // eventType : 태크
        // "item" 의 START_TAG 가 아니면 컨티뉴
        int cnt=0;
        String startTag="";
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_TAG:
                    startTag=parser.getName();
                    if(startTag.equals("item")){
                        mountainVO=new MountainVO("","","","","","","","","","");
                    }
                    if(startTag.equals("aeatreason")){
                        mntPickReason=parser.getText();
                        System.out.println("mntPickReason: "+mntPickReason);
                       // mountainVO.setMntPickReason(mntPickReason);
                    }
                    if(startTag.equals("areanm")){
                        mntLocation=parser.getText();
                        //mountainVO.setMntLocation(mntLocation);
                    }
                    if(startTag.equals("details")){
                        mntDetail=parser.getText();
                        //mountainVO.setMntDetail(mntDetail);
                    }
                    if(startTag.equals("etccourse")){
                        mntCourse=parser.getText();
                       // mountainVO.setMntCourse(mntCourse);
                    }
                    if(startTag.equals("mntheight")){
                        mntHeight=parser.getText();
                       // mountainVO.setMntHeight(mntHeight);
                    }
                    if(startTag.equals("mntnm")){
                        mntName=parser.getText();
                        //mountainVO.setMntName(mntName);
                    }
                    if(startTag.equals("tourisminf")){
                        mntTourInfo=parser.getText();
                       // mountainVO.setMntTourInfo(mntTourInfo);
                    }
                    if(startTag.equals("transport")){
                        mntTransport=parser.getText();
                      //  mountainVO.setMntTransport(mntTransport);
                    }
                    if(startTag.equals("overview")){
                        mntOverview=parser.getText();
                       // mountainVO.setMntOverview(mntOverview);
                    }
                    if(startTag.equals("subnm")){
                        mntSubName=parser.getText();
                        System.out.println("parser.getText() : "+parser.getText());
                      //  mountainVO.setMntSubName(mntSubName);
                    }

                    //객체 생성

                    break;
                case XmlPullParser.END_TAG:
                    Log.d(TAG,"END태그");
                    if(parser.getName().equals("item")){
                        System.out.println("mntName : "+mntName);
                        //mountainVO=new MountainVO(mntName,mntSubName,mntHeight,mntLocation,mntCourse,mntPickReason,mntDetail,mntOverview,mntTransport,mntTourInfo);
                        System.out.println("mountainVO.getMntPickReason() : "+mountainVO.getMntPickReason());
                        mArrayList.add(mountainVO);
                        System.out.println(mountainVO.getMntCourse());
                        Log.e(TAG,"mArrayList : "+mArrayList.size());
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case XmlPullParser.TEXT:
                    Log.d(TAG,"TEXT태그");
                    if(parser.getText()!=null){
                        System.out.println("parser.getText() in XmlPullParser.TEXT: "+parser.getText());
                        text=parser.getText();

                        switch(startTag){
                            case "aeatreason" :
                                mntPickReason=text;
                                mountainVO.setMntPickReason(text);
                                break;
                            case "areanm" :
                                mntLocation=parser.getText();
                                mountainVO.setMntLocation(mntLocation);
                                break;
                            case "details" :
                                mntDetail=parser.getText();
                                mountainVO.setMntDetail(mntDetail);
                                break;
                            case "etccourse" :
                                mntCourse=parser.getText();
                                 mountainVO.setMntCourse(mntCourse);
                                break;
                            case "mntheight" :
                                mntHeight=parser.getText();
                                mountainVO.setMntHeight(mntHeight);
                                break;
                            case "mntnm" :
                                mntName=parser.getText();
                                mountainVO.setMntName(mntName);
                                break;
                            case "transport" :
                                mntTransport=parser.getText();
                                mountainVO.setMntTransport(mntTransport);
                                break;
                            case "tourisminf" :
                                mntTourInfo=parser.getText();
                                mountainVO.setMntTourInfo(mntTourInfo);
                                break;
                            case "overview" :
                                mntOverview=parser.getText();
                                mountainVO.setMntOverview(mntOverview);
                                break;
                            case "subnm" :
                                mntSubName=parser.getText();
                                mountainVO.setMntSubName(mntSubName);
                                break;
                        }

                    }
                    break;
            }
            try {
                eventType = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }


            mAdapter.notifyDataSetChanged();
        for(int i=0;i<mArrayList.size();i++){
          /*  Log.d(TAG,"mArrayList.get(i).getMntName() : "+mArrayList.get(i).getMntName());
            Log.d(TAG,"mArrayList.get(i).getMntSubName() : "+mArrayList.get(i).getMntSubName());
            Log.d(TAG," mArrayList.get(i).getMntHeight() : "+ mArrayList.get(i).getMntHeight());
            Log.d(TAG,"mArrayList.get(i).getMntLocation() : "+mArrayList.get(i).getMntLocation());
            Log.d(TAG,"mArrayList.get(i).getMntCourse() : "+mArrayList.get(i).getMntCourse());
            Log.d(TAG,"mArrayList.get(i).getMntPickReason() : "+mArrayList.get(i).getMntPickReason());
            Log.d(TAG,"mArrayList.get(i).getMntDetail() : "+mArrayList.get(i).getMntDetail());
            Log.d(TAG,"mArrayList.get(i).getMntOverview() : "+mArrayList.get(i).getMntOverview());
            Log.d(TAG,"mArrayList.get(i).getMntTransport() : "+mArrayList.get(i).getMntTransport());
            Log.d(TAG,"mArrayList.get(i).getMntTourInfo() : "+mArrayList.get(i).getMntTourInfo());
*/
            System.out.println("mArrayList.get(i).getMntTourInfo() : "+i+":"+mArrayList.get(i).getMntTourInfo());
            System.out.println("mArrayList.get(i).getMntName() : "+i+":"+mArrayList.get(i).getMntName());
            System.out.println("mArrayList.get(i).getMntSubName() : "+i+":"+mArrayList.get(i).getMntSubName());
            System.out.println(" mArrayList.get(i).getMntHeight() : "+i+":"+ mArrayList.get(i).getMntHeight());
            System.out.println("mArrayList.get(i).getMntLocation() : "+i+":"+mArrayList.get(i).getMntLocation());
            System.out.println("mArrayList.get(i).getMntCourse() : "+i+":"+mArrayList.get(i).getMntCourse());
            System.out.println("mArrayList.get(i).getMntPickReason() : "+i+":"+mArrayList.get(i).getMntPickReason());
            System.out.println("mArrayList.get(i).getMntDetail() : "+i+":"+mArrayList.get(i).getMntDetail());
            System.out.println("mArrayList.get(i).getMntOverview() : "+i+":"+mArrayList.get(i).getMntOverview());
            System.out.println("mArrayList.get(i).getMntTransport() : "+i+":"+mArrayList.get(i).getMntTransport());
            System.out.println("mArrayList.get(i).getMntTourInfo() : "+i+":"+mArrayList.get(i).getMntTourInfo());
        }


        System.out.println("mArrayList.size() : "+mArrayList.size());

        //while
        Log.e(TAG,"// get API Data");
        mAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Log.e(TAG,"클릭 ");
                Intent intent=new Intent(MountainListActivity.this,MountainReadActivity.class);
                intent.putExtra("mntName",mArrayList.get(pos).getMntName());
                intent.putExtra("mntSubName",mArrayList.get(pos).getMntSubName());
                intent.putExtra("mntHeight",mArrayList.get(pos).getMntHeight());
                intent.putExtra("mntLocation",mArrayList.get(pos).getMntLocation());
                intent.putExtra("mntCourse",mArrayList.get(pos).getMntCourse());
                intent.putExtra("mntPickReason",mArrayList.get(pos).getMntPickReason());
                intent.putExtra("mntDetail",mArrayList.get(pos).getMntDetail());
                intent.putExtra("mntOverview",mArrayList.get(pos).getMntOverview());
                intent.putExtra("mntTransport",mArrayList.get(pos).getMntTransport());
                intent.putExtra("mntTourInfo",mArrayList.get(pos).getMntTourInfo());
                startActivity(intent);
            }
            @Override
            public void OnDeleteClick(View v, int pos) {

            }
        });
    } //onCreate

    @Override
    public void onClick(View view) {

    }
}