package com.spring.receiveaudio_server.stt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
public class RTZStt {
    private final OkHttpClient client;
    private final RTZRWebSocketListener webSocketListener;
    private WebSocket rtzrWebSocket;
    public RTZStt() {
        this.client = new OkHttpClient();
        this.webSocketListener = new RTZRWebSocketListener();
    }

    public String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", "nTOIg0lT6SSNuGbuS9AJ")
                .add("client_secret", "MNCm0fStdiZB0BwKBuiVYVftQkWTYei92q3Pknl9")
                .build();

        Request request = new Request.Builder()
                .url("https://openapi.vito.ai/v1/authenticate")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> map = objectMapper.readValue(response.body().string(), HashMap.class);

        return map.get("access_token");
    }

    public void openConnection(String token) throws Exception {
        // ... WebSocket 연결 설정 ...
        HttpUrl.Builder httpBuilder = HttpUrl.get("https://openapi.vito.ai/v1/transcribe:streaming").newBuilder();
        httpBuilder.addQueryParameter("sample_rate","8000");
        httpBuilder.addQueryParameter("encoding","LINEAR16");
        httpBuilder.addQueryParameter("use_itn","true");
        httpBuilder.addQueryParameter("use_disfluency_filter","true");
        httpBuilder.addQueryParameter("use_profanity_filter","true");

        String url = httpBuilder.toString().replace("https://", "wss://");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+token)
                .build();
        rtzrWebSocket = client.newWebSocket(request, webSocketListener);
    }
    public String stt(byte[] audioBuf, String token) throws Exception {
        if(rtzrWebSocket == null){
            openConnection(token);
        }
        File file = new File("/Users/ailak/Desktop/http-spring/practice_Spring_Boot/SendAudio-http2/receiveAudio_Server/src/main/resources/audio/test.wav");
        AudioInputStream in = AudioSystem.getAudioInputStream(file);

        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            rtzrWebSocket.send(ByteString.of(buffer));
        }
//        rtzrWebSocket.send(ByteString.of(audioBuf));
        client.dispatcher().executorService().shutdown();
        log.info(webSocketListener.getTranscriptionResult());
        // "end" 신호를 받으면 WebSocket 연결 종료
        if (new String(audioBuf).equals("end")) {
            rtzrWebSocket.close(1000, "End of Stream");
            log.info(webSocketListener.getTranscriptionResult());
            return webSocketListener.getTranscriptionResult();
        } else {
            return null; // 실시간 응답이 없는 경우 null 반환
        }

    }
}


