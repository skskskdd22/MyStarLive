package com.example.owner.mystarlive;

import android.os.AsyncTask;
import android.util.Log;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import java.util.Arrays;

/*
 * 구글번역API가져와서 백그라운드 프로세스에서 텍스트번역을 할 수있게 하는 클래스
 */
public class GoogleTranslate extends AsyncTask<String, Void, String> {


    private final String API_KEY = "AIzaSyC_j78QnDEZzL_qRPAdQ7lXEwCYRwReh0Y";

    @Override
    protected String doInBackground(String... params){

        //번역 될 텍스트
        final String textToTranslate = params[0];

       //번역 할 소스 언어
        final String SOURCE_LANGUAGE = params[1];

        //번역 할 언어
        final String TARGET_LANGUAGE = params[2];

        try {

            NetHttpTransport netHttpTransport 	= new NetHttpTransport();
            JacksonFactory jacksonFactory 		= new JacksonFactory();
            // Google Translate객체 생성
            Translate translate = new Translate.Builder(netHttpTransport, jacksonFactory, null).build();

            //텍스트 번역, API 키 및 대상 언어 설정
            Translate.Translations.List listToTranslate = translate.new Translations().list(
                    Arrays.asList(textToTranslate), TARGET_LANGUAGE).setKey(API_KEY);


            //번역 될 텍스트의 언어를 설정
            listToTranslate.setSource(SOURCE_LANGUAGE);

           // 번역실행
            TranslationsListResponse response = listToTranslate.execute();

            return response.getTranslations().get(0).getTranslatedText();
        } catch (Exception e){

            Log.e("Google Response ", e.getMessage());

            /*
             * I would return empty string if there is an error
             * to let the method which invoked the translating method know that there is an error
             * and subsequently it deals with it
             */

            return "";
        }
    }
}