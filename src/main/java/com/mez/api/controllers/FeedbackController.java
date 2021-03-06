package com.mez.api.controllers;

import com.mez.api.models.FeedBack;
import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.bots.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

  private final TelegramBot telegramBot;

  @Autowired
  FeedbackController(TelegramBot bot) {
    this.telegramBot = bot;
  }

  @RequestMapping("/feedback")
  public int feedBack(@RequestBody FeedBack feedBack) {
    String message;
    if (feedBack.getMessage() != null && feedBack.getMessage().startsWith("Когда позвонить: ")) {
      message = "Заказ на телефонный звонок";
      message += "\nимя: " + feedBack.getName();
      message += "\nтелефон: " + feedBack.getContact();
      if (feedBack.getMessage().length() > 18) {
        message += "\n" + feedBack.getMessage();
      }
    } else {
      message = "Новое сообщение!";
      message += "\nконтакт: " + feedBack.getContact();
      if (feedBack.getMessage() != null && feedBack.getMessage().length() > 0) {
        message += "\nсообщение: " + feedBack.getMessage();
      }
    }
    return telegramBot.sendMessage(message) ?
        ResponseCodes.SUCCESS :
        ResponseCodes.UNKNOWN_ERROR;
  }
}
