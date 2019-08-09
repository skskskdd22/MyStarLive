package com.example.owner.mystarlive.rtc_peer.kurento.models.response;

import com.example.owner.mystarlive.rtc_peer.kurento.models.CandidateModel;
import com.example.owner.mystarlive.rtc_peer.kurento.models.IdModel;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 서버로 부터 받은 응답에 대한 키값을 받을 수 있도록 정의해놓은 파일
 * 채팅, 음성인식, 유저이름이 추가됨
 */

public class ServerResponse extends IdModel implements Serializable {
    @SerializedName("response")
    private String response;
    @SerializedName("sdpAnswer")
    private String sdpAnswer;
    @SerializedName("candidate")
    private CandidateModel candidate;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("from")
    private String from;
    @SerializedName("chat")
    private String chat;
    @SerializedName("name")
    private String name;
    @SerializedName("userid")
    private String userid;
    @SerializedName("voice")
    private String voice;

    public IdResponse getIdRes() {
        return IdResponse.getIdRes(getId());
    }

    public TypeResponse getTypeRes() {
        return TypeResponse.getType(getResponse());
    }

    public String getResponse() {
        return response;
    }

    public String getSdpAnswer() {
        return sdpAnswer;
    }

    public CandidateModel getCandidate() {
        return candidate;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFrom() {
        return from;
    }

    public String getchat() {
        return chat;
    }

    public String getname() { return name; }

    public String getuserid() { return userid; }

    public String getvoice() { return voice; }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
