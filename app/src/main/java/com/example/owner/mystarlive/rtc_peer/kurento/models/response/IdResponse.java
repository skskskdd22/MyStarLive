package com.example.owner.mystarlive.rtc_peer.kurento.models.response;

/**
 * 서버에서 받은 아이디값(serverResponse.getIdRes())을 정의해놓은 파일
 * 여기에 채팅관련 및 음성인식관련 응답아이디값을 추가하였다.
 */

public enum IdResponse {

    REGISTER_RESPONSE("registerResponse"),
    PRESENTER_RESPONSE("presenterResponse"),
    ICE_CANDIDATE("iceCandidate"),
    VIEWER_RESPONSE("viewerResponse"),
    STOP_COMMUNICATION("stopCommunication"),
    CLOSE_ROOM_RESPONSE("closeRoomResponse"),
    INCOMING_CALL("incomingCall"),
    START_COMMUNICATION("startCommunication"),
    CALL_RESPONSE("callResponse"),
    CHAT_RESPOSE("chatmessage"),
    VOICE_RESPOSE("voicemessage"),

    UN_KNOWN("unknown");

    private String id;

    IdResponse(String id) {
        this.id = id;
    }

    public static IdResponse getIdRes(String idRes) {
        for (IdResponse idResponse : IdResponse.values()) {
            if (idRes.equals(idResponse.getId())) {
                return idResponse;
            }
        }
        return UN_KNOWN;
    }

    public String getId() {
        return id;
    }
}
