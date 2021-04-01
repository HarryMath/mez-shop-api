package com.mez.api.controllers;

import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FeedbackController {

    private final TelegramBot telegramBot;
    @Autowired
    FeedbackController(TelegramBot bot) {
        this.telegramBot = bot;
    }

    @RequestMapping("/feedback")
    public int feedBack(@RequestParam Map<String, String> parameters){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            message.append(entry.getKey())
                    .append(":%20")
                    .append(entry.getValue())
                    .append("%0D%0A");
        }
        return telegramBot.sendMessage(message.toString()) ?
                ResponseCodes.SUCCESS :
                ResponseCodes.UNKNOWN_ERROR;
    }
}
