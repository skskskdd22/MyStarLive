package com.example.owner.mystarlive.rtc_peer.kurento;

import android.util.Log;

import com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.nhancv.webrtcpeer.rtc_peer.RTCClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

/*
 * 방송자 클라이언트 클래스
 * 방송자의 네트워크 정보와 방값을 전달한다.
 */

public class KurentoPresenterRTCClient implements RTCClient {
    private static final String TAG = KurentoPresenterRTCClient.class.getSimpleName();

    private SocketService socketService;
    private String roomname;


    public KurentoPresenterRTCClient(SocketService socketService, String roomname) {
        this.socketService = socketService;
        this.roomname = roomname;
    }

    public void connectToRoom(String host, BaseSocketCallback socketCallback) {
        socketService.connect(host, socketCallback);
    }

    @Override
    public void sendOfferSdp(SessionDescription sdp) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "presenter");
            obj.put("sdpOffer", sdp.description);
            obj.put("name", roomname);
            Log.e("SDP전달내용 : ", String.valueOf(obj));

            socketService.sendMessage(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAnswerSdp(SessionDescription sdp) {
        Log.e(TAG, "sendAnswerSdp: ");
    }

    @Override
    public void sendLocalIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "onIceCandidate");
            JSONObject candidate = new JSONObject();
            candidate.put("candidate", iceCandidate.sdp);
            candidate.put("sdpMid", iceCandidate.sdpMid);
            candidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            obj.put("candidate", candidate);
            obj.put("name", roomname);
            Log.e("SDP전달내용 : ", String.valueOf(obj));

            socketService.sendMessage(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
 //서버로 채팅전달
    public void sendChatmassage(String chatmassge) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "chatmessage");
            obj.put("name", roomname);
            obj.put("chat", chatmassge);
            Log.e("SDP전달내용 : ", String.valueOf(obj));

            socketService.sendMessage(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //서버로 음성인식된 텍스트전송
    public void sendvoicemessage(String voicemessage) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "voicemessage");
            obj.put("name", roomname);
            obj.put("voice", voicemessage);
            Log.e("SDP전달내용 : ", String.valueOf(obj));

            socketService.sendMessage(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //스트리밍 중단
    public void sendstopmassage() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", "stop");
            obj.put("name", roomname);
            Log.e("SDP전달내용 : ", String.valueOf(obj));

            socketService.sendMessage(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] candidates) {
        Log.e(TAG, "sendLocalIceCandidateRemovals: ");
    }

}
