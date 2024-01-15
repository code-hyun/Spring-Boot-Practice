package com.spring.receiveaudio_server.stt;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

@Component
public class RTZStt {
    RTZAuth rtzAuth;
    RTZRWebSocketListener rtzWebSocketListener;

    public void stt(byte[] audioBuf) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String token = rtzAuth.authentication();
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

        RTZRWebSocketListener webSocketListener = new RTZRWebSocketListener();
        WebSocket rtzrWebSocket = client.newWebSocket(request, webSocketListener);

//        File file = new File("sample.wav");
//        AudioInputStream in = AudioSystem.getAudioInputStream(file);
//
//        byte[] buffer = new byte[1024];
//        while (in.read(buffer) != -1) {
            rtzrWebSocket.send(ByteString.of(audioBuf));
//        }

        client.dispatcher().executorService().shutdown();
    }

}
