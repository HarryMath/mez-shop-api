package com.mez.api.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

@Component
public class TelegramBot {

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.chatId}")
    private String chatId;

    public boolean sendMessage(String message) {
        try {
            String response = makeRequest("https://api.telegram.org/bot" + token +
                    "/sendMessage?chat_id=" + chatId +
                    "&text=" + message
            );
            return response.startsWith("{\"ok\":true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendAsync(String message) {
        Thread sendingThread = new Thread("sending Thread") {
            @Override
            public void run(){
                sendMessage(message);
            }
        };
        sendingThread.start();
    }

    private String makeRequest(String address) throws IOException {
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.lines().collect(Collectors.joining());
    }
}
