package com.example.owner.mystarlive.broadcaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.owner.mystarlive.R;
import com.example.owner.mystarlive.SpeechAPI;
import com.example.owner.mystarlive.VoiceRecorder;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nhancv.npermission.NPermission;
import com.nhancv.webrtcpeer.rtc_plugins.ProxyRenderer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 방송자 Activity부분
 * 방이름값을 받아  BroadCasterPresenter로 전달하여 방을 나눠준다.
 * 방송자의 음성을 인식해서 자막으로 보내주고 채팅을 할 수있게 해준다.
 */
@EActivity(R.layout.activity_broadcaster)
public class BroadCasterActivity extends MvpActivity<BroadCasterView, BroadCasterPresenter>
        implements BroadCasterView, NPermission.OnPermissionResult {
    private static final String TAG = BroadCasterActivity.class.getSimpleName();

    @ViewById(R.id.vGLSurfaceViewCall)
    protected SurfaceViewRenderer vGLSurfaceViewCall;

    private NPermission nPermission;
    private EglBase rootEglBase;
    private ProxyRenderer localProxyRenderer;
    private Toast logToast;
    private boolean isGranted;

    @ViewById(R.id.chattext)
    protected EditText chattext;
    @ViewById(R.id.send_button)
    protected Button send_button;
   // @ViewById(R.id.chatmessage)
  //  protected TextView chatmessage;
    @ViewById(R.id.scrollView)
    protected ScrollView scrollView;
    @ViewById(R.id.container)
    protected LinearLayout container;


    /*Speech to Text관련 변수선언*/
    private static final int RECORD_REQUEST_CODE = 101;
    @BindView(R.id.listview)
    ListView listView;
    private List<String> stringList;
    private SpeechAPI speechAPI;
    private VoiceRecorder mVoiceRecorder;
    private ArrayAdapter adapter;
    String text;


    @AfterViews
    protected void init() {
        ButterKnife.bind(this);
        speechAPI = new SpeechAPI(BroadCasterActivity.this);
        stringList = new ArrayList<>();
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(adapter);


        //MainActivity로부터 받은 방이름값
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("roomname");
        setResult(RESULT_OK, intent); //응답 전달 후

        nPermission = new NPermission(true);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        //config peer
        localProxyRenderer = new ProxyRenderer();
        rootEglBase = EglBase.create();
        //speechAPI = new SpeechAPI(BroadCasterActivity.this);
        vGLSurfaceViewCall.init(rootEglBase.getEglBaseContext(), null);
        vGLSurfaceViewCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        vGLSurfaceViewCall.setEnableHardwareScaler(true);
        vGLSurfaceViewCall.setMirror(true);
        localProxyRenderer.setTarget(vGLSurfaceViewCall);
        presenter.initPeerConfig(name);
    }

    @Override
    public void disconnect() {
        localProxyRenderer.setTarget(null);
        if (vGLSurfaceViewCall != null) {
            vGLSurfaceViewCall.release();
            vGLSurfaceViewCall = null;
        }
        finish();
    }

    /*
    목소리가 들리면 음성녹음을 진행한다.
     */
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (speechAPI != null) {
                speechAPI.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (speechAPI != null) {
                speechAPI.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (speechAPI != null) {
                speechAPI.finishRecognizing();
            }
        }

    };

    //음성인식이 된 내용을 텍스트화해서 스트리밍 서버로 전달한다.
    private final SpeechAPI.Listener mSpeechServiceListener =
            new SpeechAPI.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (!TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    Log.d("isFinal",text);
                                    presenter.sendvoicemessage(text);
                                    voiceAndToast(text);
                                  //  textMessage.setText(null);
                                    /*stringList.add(0,text);
                                    if(stringList.size() > 1 ){
                                        stringList.remove(1);
                                    }
                                    adapter.notifyDataSetChanged();*/
                                } else {
                               //     textMessage.setText(text);
                                }
                            }
                        });
                    }
                }
            };


    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    /*
    * onStart시에 음성인식과 텍스트화진행
    * 오디오에 대한 권한 요청진행
    * */
    @Override
    protected void onStart() {
        super.onStart();
        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else {
            makeRequest(Manifest.permission.RECORD_AUDIO);
        }
        speechAPI.addListener(mSpeechServiceListener);
    }
    private int isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }
    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
    }


    /*
    * SKD버전 23이하일 때 방송송출시작 / SDK버전 23이상일 경우 카메라에 대한 권한요청
    * */
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 23 || isGranted) {
            presenter.startCall();
        } else {
            nPermission.requestPermission(this, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            //카메라 권한관련 동의 시 스트리밍진행, 미동의 시 권한 재요청
            case Manifest.permission.CAMERA:
                this.isGranted = isGranted;
                if (!isGranted) {
                    nPermission.requestPermission(this, Manifest.permission.CAMERA);
                } else {
                    //nPermission.requestPermission(this, Manifest.permission.RECORD_AUDIO);
                    presenter.startCall();
                }
                break;
            default:
                break;
        }
    }

    /*소켓생성*/
    @NonNull
    @Override
    public BroadCasterPresenter createPresenter() {
        return new BroadCasterPresenter(getApplication());
    }


    /*뒤로가기버튼 클릭 시 방송자연결끊김*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.sendstopmessage();
        presenter.disconnect();
    }

    /*문자열을 받아서 토스트로 띄우는 메소드
    * 서버로부터 받은 것들을 확인할때 사용함*/
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

    //서버로 부터 받은 채팅메세지 뷰에 띄우는 메소드
    @Override
    public void chatmessage(String text) {
        stringList.add(text);
        adapter.notifyDataSetChanged();
    }


    @Override
    public VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        if (useCamera2()) {
            if (!captureToTexture()) {
                return null;
            }
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
        }
        if (videoCapturer == null) {
            return null;
        }
        return videoCapturer;
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return rootEglBase.getEglBaseContext();
    }

    @Override
    public VideoRenderer.Callbacks getLocalProxyRenderer() {
        return localProxyRenderer;
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this) && presenter.getDefaultConfig().isUseCamera2();
    }

    private boolean captureToTexture() {
        return presenter.getDefaultConfig().isCaptureToTexture();
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();
        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

}
