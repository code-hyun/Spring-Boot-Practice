package com.spring.receiveaudio_server.stt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RTZRWebSocketListener extends WebSocketListener{
    private static final Logger logger = Logger.getLogger(RTZRWebSocketListener.class.getName());
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private String transcriptionResult;

    private static void log(Level level, String msg, Object... args) {
        logger.log(level, msg, args);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log(Level.INFO, "Open " + response.message());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(text);
            JsonNode alternativesNode = rootNode.path("alternatives");
            if (alternativesNode.isArray()) {
                for (JsonNode altNode : alternativesNode) {
                    String transcript = altNode.path("text").asText();
                    System.out.println("Transcript: " + transcript);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error parsing JSON response", e);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println(bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        log(Level.INFO, "Closing", code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();

    }

    public String getTranscriptionResult() {
        return transcriptionResult;
    }
}
