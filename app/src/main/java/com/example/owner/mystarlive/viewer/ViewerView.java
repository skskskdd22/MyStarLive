package com.example.owner.mystarlive.viewer;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.webrtc.EglBase;
import org.webrtc.VideoRenderer;

/**
 * view로 보여주는 부분을 바꾸고 싶으면 여기서 선언해주고
 * Activity에 작업해줘야한다.
 */

public interface ViewerView extends MvpView {
    //서버로 부터 받은 채팅메세지 뷰에 띄우는 메소드
    void chatmessage(String text);

    //서버로 부터 받은 채팅메세지 뷰에 띄우는 메소드
    void texttranslate(String text);

    void stopCommunication();

    void logAndToast(String msg);

    void disconnect();

    /*음성텍스트 사용할 때만 사용되는
     * 토스트메소드 */
    void voiceAndToast(String msg);

    EglBase.Context getEglBaseContext();

    VideoRenderer.Callbacks getRemoteProxyRenderer();

    void initListener(String msg);
}
