package com.example.owner.mystarlive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;

/*테스트하는 액티비티*/
public class SpeechActivity extends AppCompatActivity {



    ActionBar actionBar;
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.Home:
                    transaction.replace(R.id.content, new HomeFragment()).commit();
                    return true;
                case R.id.MediaPlay:
                    /*Vod 페이지를 불러온다.*/
                    transaction.replace(R.id.content, new MediaPlayFragment()).commit();
                    return true;

                case R.id.GoodsList:
                    /*Vod 페이지를 불러온다.*/
                    transaction.replace(R.id.content, new GoodsFragment()).commit();
                    return true;

                case R.id.User:
                    /*Vod 페이지를 불러온다.*/
                    transaction.replace(R.id.content, new UserFragment()).commit();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        actionBar=getSupportActionBar();
        BottomNavigationView bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, new HomeFragment()).commit();

/*
        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("http://www.earlyadopter.co.kr/wp-content/uploads/2019/06/netmarble-bts-world-teaser-2-1024x576.jpg");
        data.add("https://i.ytimg.com/vi/UMoD_57GlNo/maxresdefault.jpg");
        data.add("https://image.fmkorea.com/files/attach/new/20180730/3655109/1161611/1184974504/cbdb3501bad17fc939181301203130a5.png");
        data.add("https://exo-jp.net/common/images/update/slide_20180509cbx.jpg");

        autoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        vodrecyclerView = findViewById(R.id.VODStreaming);
        liverecyclerView = findViewById(R.id.LiveStreaming);

        Livelist();
        Vodlist();*/
    }

    /*retrofit을 이용해서 서버에서 Live목록을 json형태로 가져온다
    private void Livelist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getlivelist();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        Log.e("jsonresponse", jsonresponse);

                        writeLive(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /*Recyclerview로 가져온 Live목록을 뿌려준다.
    private void writeLive(String response){

        try {

                JSONArray liveArray  = new JSONArray(response);
                Log.e("dataArray", String.valueOf(liveArray));
            LiveRecyclerArrayList = new ArrayList<Live>();
                for (int i = 0; i < liveArray.length(); i++) {

                    Live modelRecycler = new Live();
                    JSONObject dataobj = liveArray.getJSONObject(i);
                    Log.e("dataobj", String.valueOf(dataobj));

                    Log.e("dataobj.getString : ", dataobj.getString("live_id"));
                    modelRecycler.setImgURL(dataobj.getString("live_image"));
                    modelRecycler.setliveid(dataobj.getString("live_id"));
                    modelRecycler.setlivetitle(dataobj.getString("live_title"));

                    LiveRecyclerArrayList.add(modelRecycler);

                }

            liveAdapter = new LiveAdapter(this,LiveRecyclerArrayList);
            liverecyclerView.setAdapter(liveAdapter);
            liverecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*retrofit을 이용해서 서버에서 Vod목록을 json형태로 가져온다
    private void Vodlist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getvoidlist();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("VODResponsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        Log.e("VODjsonresponse", jsonresponse);

                        writeVod(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /*Recyclerview로 가져온 Vod목록을 뿌려준다.
    private void writeVod(String response){

        try {

            JSONArray vodArray  = new JSONArray(response);
            Log.e("vodArray", String.valueOf(vodArray));
            VodRecyclerArrayList = new ArrayList<Vod>();
            for (int i = 0; i < vodArray.length(); i++) {

                Vod vodRecycler = new Vod();
                JSONObject vodobj = vodArray.getJSONObject(i);
                Log.e("dataobj", String.valueOf(vodobj));
                String id = "유저 " + vodobj.getString("vod_id");
                Log.e("dataobjid :", id);

                Log.e("dataobj.getString : ", vodobj.getString("vod_id"));
                vodRecycler.setImgURL(vodobj.getString("vod_image"));
                vodRecycler.setvodid(id);
                vodRecycler.setvodtitle(vodobj.getString("vod_title"));

                VodRecyclerArrayList.add(vodRecycler);

            }

            vodAdapter = new VodAdapter(this,VodRecyclerArrayList);
            vodrecyclerView.setAdapter(vodAdapter);
            vodrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
