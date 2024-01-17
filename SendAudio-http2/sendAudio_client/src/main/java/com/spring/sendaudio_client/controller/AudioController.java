package com.spring.sendaudio_client.controller;

import com.spring.sendaudio_client.protocol.UserHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AudioController {
    static final int chunkSize = 640;

//    private UserHeader userHeader;
//    private HttpHeaders headers;
    @PostMapping("/request")
    public String sendAudio(@RequestBody byte[] audioBuf, @RequestHeader Map<String, String> header) {
        log.info("tid {} size : {} vrCodec {}", header.get("tid"), audioBuf.length, header.get("vrcodec"));

//        ssl 검사 X 메소드
//        trustSSL();

//        userHeader = new UserHeader(header.get("tid"),"vin_test", header.get("vrcodec"));
//        headers = new HttpHeaders();
        UserHeader userHeader = new UserHeader(header.get("tid"), "vin_test", header.get("vrcodec"));
        HttpHeaders headers = new HttpHeaders();

        headers.add("tid", userHeader.getTid());
        headers.add("vin", userHeader.getVin());
        headers.add("vrCodec", userHeader.getVrCodec());
        headers.add("content-type", "application/json");

        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8080/receiveAudio";
        String url = "https://localhost:442/receiveAudio";
        int offset = 0;

        try {
            while (offset < audioBuf.length) {
                int remainByte = Math.min(chunkSize, audioBuf.length - offset);
                byte[] chunk = new byte[remainByte];
                // arraycopy(원본, 원본의 어느 부분부터, 복사할 대상, 복사할 원본의 처음, 복사할 원본 끝)
                System.arraycopy(audioBuf, offset, chunk, 0, remainByte);

                HttpEntity<byte[]> entity = new HttpEntity<>(chunk, headers);
                restTemplate.postForObject(url, entity, String.class);

                offset += chunkSize;
            }

            HttpEntity<String> entity = new HttpEntity<>("end", headers);
            String finalizeResponse = restTemplate.postForObject(url, entity, String.class);
            System.out.println("Response: " + finalizeResponse);

            if (Objects.equals(finalizeResponse, "end")) {
                return "ok";
            }else {
                log.info(finalizeResponse);
                return "";
            }
        } catch (RestClientException e) {
            log.error("REST 요청 오류: {}", e.getMessage());
            return e.getMessage();
        }
    }


//    public static void trustSSL() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[] {
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
//                        @Override
//                        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
//                        @Override
//                        public X509Certificate[] getAcceptedIssuers() {
//                            return new X509Certificate[0];
//                            //return null;
//                        }
//                    }
//            };
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
