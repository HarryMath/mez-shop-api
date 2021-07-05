package com.mez.api.tools.bots;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class MailBot {

  private final JavaMailSender mailSender;
  private final String FROM = "MEZ-SHOP-BOT <mezShopBot@gmail.com>";

  @Autowired
  public MailBot(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @ResponseBody
  public boolean send(String mailTo, String title, String text) {
    try {
      InternetAddress from = new InternetAddress("mezShopBot@gmail.com", "MEZ-SHOP-BOT");
      MimeMessage message = mailSender.createMimeMessage();
      message.setFrom(from);
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
      messageHelper.setFrom(from);
      messageHelper.setTo(mailTo);
      messageHelper.setSubject(title);
      messageHelper.setText(text, true);
      mailSender.send(message);
      return true;
    } catch (Exception e) {
      System.out.println("Message wasn't send: " + e.getMessage());
      return false;
    }
  }
}
