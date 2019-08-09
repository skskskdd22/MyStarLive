package com.example.owner.mystarlive.broadcaster;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.webrtc.EglBase;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

/**
 * view로 보여주는 부분을 바꾸고 싶으면 여기서 선언해주고
 * Activity에 작업해줘야한다.
 */

public interface BroadCasterView extends MvpView {

    void logAndToast(String msg);

    void voiceAndToast(String msg);

    void disconnect();

    VideoCapturer createVideoCapturer();

    EglBase.Context getEglBaseContext();

    VideoRenderer.Callbacks getLocalProxyRenderer();

    void initListener(String ii);

    void chatmessage(String text);


}
