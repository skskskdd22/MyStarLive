package com.example.owner.mystarlive.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.owner.mystarlive.GoogleTranslate;
import com.example.owner.mystarlive.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 스트리밍 시청자가 스트리밍을 보는 액티비티
 * 방값을 받아서 스트리밍 방에 들어가서 채팅할 수있다.
 */
@EActivity(R.layout.activity_broadcaster)
public class ViewerActivity extends MvpActivity<ViewerView, ViewerPresenter> implements ViewerView {
    private static final String TAG = ViewerActivity.class.getSimpleName();

    @ViewById(R.id.vGLSurfaceViewCall)
    protected SurfaceViewRenderer vGLSurfaceViewCall;
    private EglBase rootEglBase;
    private ProxyRenderer remoteProxyRenderer;
    private Toast logToast;

    /*채팅전송변수*/
    @ViewById(R.id.chattext)
    protected EditText chattext;
    @ViewById(R.id.send_button)
    protected Button send_button;
    @BindView(R.id.listview)
    ListView listView;
    private List<String> stringList;
    private ArrayAdapter adapter;
    String text;

    //GoogleTranslate googleTranslate;

    @AfterViews
    protected void init() {
        ButterKnife.bind(this);
        stringList = new ArrayList<>();
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(adapter);



        //메인에서 스트리밍 방값을 받는다.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("roomname");
        setResult(RESULT_OK, intent); //응답 전달 후

        remoteProxyRenderer = new ProxyRenderer();
        rootEglBase = EglBase.create();

        vGLSurfaceViewCall.init(rootEglBase.getEglBaseContext(), null);
        vGLSurfaceViewCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        vGLSurfaceViewCall.setEnableHardwareScaler(true);
        vGLSurfaceViewCall.setMirror(true);
        remoteProxyRenderer.setTarget(vGLSurfaceViewCall);

        presenter.initPeerConfig(name);
        presenter.startCall();
    }

    /*채팅을 입력하고 보내기 버튼 클릭 시 텍스트 서버전달*/
    @Override
    public void initListener(String ii){
        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chattext=(EditText) findViewById(R.id.chattext);
                text = chattext.getText().toString();
                //logAndToast(text);
                //logAndToast(ii);
                Log.e("버튼클릭됨 : ",text);
                presenter.sendchatmessage(text);
                chattext.setText("");
            }
        });
    }
    @Override
    public void disconnect() {
        remoteProxyRenderer.setTarget(null);
        if (vGLSurfaceViewCall != null) {
            vGLSurfaceViewCall.release();
            vGLSurfaceViewCall = null;
        }

        finish();
    }

    //서버로 부터 받은 채팅메세지 뷰에 띄우는 메소드
    @Override
    public void chatmessage(String text) {
            stringList.add(text);
            adapter.notifyDataSetChanged();

    }


    //서버로 부터 받은 채팅메세지 뷰에 띄우는 메소드
    @Override
    public void texttranslate(String text) {
        GoogleTranslate googleTranslate = new GoogleTranslate();
        try {

            Log.d(TAG, text);
            if (logToast != null) {
                logToast.cancel();
            }
            String result = googleTranslate.execute(text, "ko", "en").get();
            logToast = Toast.makeText(this, result, Toast.LENGTH_LONG);
            logToast.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        googleTranslate.cancel(true);
    }

    @NonNull
    @Override
    public ViewerPresenter createPresenter() {
        return new ViewerPresenter(getApplication());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.disconnect();
    }

    @Override
    public void stopCommunication() {
        onBackPressed();
    }

    @Override
    public void logAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        logToast.show();
    }

    /*음성텍스트 사용할 때만 사용되는
     * 토스트메소드 */
    @Override
    public void voiceAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        logToast.show();
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return rootEglBase.getEglBaseContext();
    }

    @Override
    public VideoRenderer.Callbacks getRemoteProxyRenderer() {
        return remoteProxyRenderer;
    }

}
