package com.example.owner.mystarlive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.owner.mystarlive.broadcaster.BroadCasterActivity_;
import com.example.owner.mystarlive.viewer.ViewerActivity_;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    boolean chack = true;
    String userid, mlikestar;
    SharedPreferences save;
    Dialog dialog;
    AutoScrollViewPager autoViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*로그인 저장내용 가져와서 자동로그인*/
        SharedPreferences save = getSharedPreferences("Save", Activity.MODE_PRIVATE);
        boolean loginchack = save.getBoolean("Check", false);
        userid = save.getString("userid", null);
        mlikestar = save.getString("likestar", null);



        /*DrawerLayout선언해주고 클릭시 드로어확인해줌*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_login);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);
        Button button_login = (Button) header_view.findViewById(R.id.button_login);
        LinearLayout login = (LinearLayout) header_view.findViewById(R.id.info);

        /*로그인 시 로그인한 회원정보 보여줌
         * 로그아웃시 로그인버튼활성화*/
        if(loginchack == true) {

            button_login.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);

            TextView id = (TextView) header_view.findViewById(R.id.userid);
            TextView likestar = (TextView) header_view.findViewById(R.id.likestar);

            id.setText(userid);
            likestar.setText(mlikestar);

        } else {
            button_login.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("http://d3qpgbf7vej5yf.cloudfront.net/wp-content/uploads/2019/06/netmarble-bts-world-teaser-2-1024x576.jpg");
        data.add("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg?quality=0.60");
        data.add("http://nick.mtvnimages.com/nick/video/images/nick/sb-053-16x9.jpg?maxdimension=&quality=0.60");
        data.add("https://www.gannett-cdn.com/-mm-/60f7e37cc9fdd931c890c156949aafce3b65fd8c/c=243-0-1437-898&r=x408&c=540x405/local/-/media/2017/03/14/USATODAY/USATODAY/636250854246773757-XXX-IMG-WTW-SPONGEBOB01-0105-1-1-NC9J38E8.JPG");

        autoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작








    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //네비게이션 바에 있는 아이템 클릭 시 이벤트발생
        int id = item.getItemId();

        if (id == R.id.nav_broadcast) {
            // 방송하기 버튼 클릭 시
            showPresenter(MainActivity.this);
        } else if (id == R.id.nav_view) {
            showView(MainActivity.this);
        }
        else if (id == R.id.nav_account) {
            Intent intent = new Intent(MainActivity.this, SpeechActivity.class);
            startActivityForResult(intent, 102);
        } else if (id == R.id.nav_recent) {

        }  else if (id == R.id.nav_send) {
            //로그아웃 버튼 클릭 시 저장내용 삭제 및 새로고침
            boolean loginChecked = false;
            SharedPreferences save = getSharedPreferences("Save", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = save.edit();

            editor.clear();
            editor.putBoolean("Check", loginChecked);

            editor.commit();
            onResume();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showPresenter(Activity activity){
        //방송시작할 때 방이름을 적는 곳

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog);

        final EditText et = (EditText)dialog.findViewById(R.id.et);
        Button btnok = (Button) dialog.findViewById(R.id.startlive);

        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String roomid = et.getText().toString();
                Log.d("메인 방이름 : ",roomid);
                Intent intent = new Intent(MainActivity.this, BroadCasterActivity_.class);
                Bundle bundle = new Bundle();
                bundle.putString("roomname", roomid);
                intent.putExtras(bundle);
                Log.d("메인 방이름 : ",roomid);
                dialog.dismiss();
                startActivityForResult(intent, 102);
            }
        });

        dialog.show();
    }

    public void showView(Activity activity){
        //방송시작할 때 방이름을 적는 곳


        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.viewdialog);

        final EditText et = (EditText)dialog.findViewById(R.id.et);
        Button btnok = (Button) dialog.findViewById(R.id.startlive);

        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String roomid = et.getText().toString();
                Log.d("메인 방이름 : ",roomid);
                Intent intent = new Intent(MainActivity.this, ViewerActivity_.class);
                Bundle bundle = new Bundle();
                bundle.putString("roomname", roomid);
                intent.putExtras(bundle);
                Log.d("메인 방이름 : ",roomid);
                dialog.dismiss();
                startActivityForResult(intent, 102);
            }
        });

        dialog.show();
    }




    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences save = getSharedPreferences("Save", Activity.MODE_PRIVATE);
        boolean loginchack = save.getBoolean("Check", false);
        userid = save.getString("userid", null);
        mlikestar = save.getString("likestar", null);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_login);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);
        Button button_login = (Button) header_view.findViewById(R.id.button_login);
        LinearLayout login = (LinearLayout) header_view.findViewById(R.id.info);

        if(loginchack == true) {

            button_login.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);

            TextView id = (TextView) header_view.findViewById(R.id.userid);
            TextView likestar = (TextView) header_view.findViewById(R.id.likestar);

            id.setText(userid);
            likestar.setText(mlikestar);

        } else {
            button_login.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}

