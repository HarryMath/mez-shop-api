package com.mez.api.service;

import com.mez.api.models.DTO.CartItem;
import com.mez.api.models.Engine;
import com.mez.api.models.Order;
import com.mez.api.repository.EngineRepository;
import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.bots.TelegramBot;
import com.mez.api.tools.excell.XlsxWriter;
import com.mez.api.tools.bots.MailBot;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final MailBot mailBot;
  private final XlsxWriter xlsxWriter;
  private final EngineRepository engineRepository;
  private final TelegramBot telebot;
  @Value("${mez.cheque}")
  private String cheque;

  @Autowired
  OrderService(MailBot mailBot, XlsxWriter xlsxWriter,
      EngineRepository engineRepository, TelegramBot telebot) {
    this.mailBot = mailBot;
    this.xlsxWriter = xlsxWriter;
    this.engineRepository = engineRepository;
    this.telebot = telebot;
  }

  public byte processOrder(Order order) {
    List<CartItem> items = order.getItems();
    List<Engine> engines = engineRepository.getEngines(items);
    if (engines.size() != items.size()) {
      return ResponseCodes.DATABASE_ERROR;
    }
    boolean isSend = telebot.sendMessage(
        "Новый заказ!\n"
        + composeContent(order.getItems(), "\n")
        + "\nИмя: " + order.getName()
        + "\nТелефон: " + order.getPhone()
        + "\nпочта: " + order.getMail()
    );
    if (isSend) {
      final String mailTitle = "Заказ №" + getOrderNumber() + " на сайте mez-motor.ru";
      final String mailContent =
          "<div style=\"font-size: 14px\"><div>Здравствуйте, " + order.getName() + " </div>" +
          "<h3 style=\"width:100%;border-bottom:1px solid #999999\">Вы заказали:</h3>" +
          composeContent(items, "<br>") +
          "<br><h5>Спасибо за заказ! мы скоро с вами свяжемся!</h5></div>";
      try {
        final byte[] document = generateCheque(items, engines);
        mailBot.send(order.getMail(), mailTitle, mailContent, document, "чек.pdf");
      } catch (Exception ignore) {
        mailBot.send(order.getMail(), mailTitle, mailContent +
            "<br><h4 style=\"margin-bottom:1px\">Счёт для оплаты:</h4>" + cheque);
      }
      return ResponseCodes.SUCCESS;
    } else {
      return ResponseCodes.UNKNOWN_ERROR;
    }
  }

  private int getOrderNumber() {
    return (int) Math.round(1 + Math.random() * 9999);
  }

  private String composeContent(List<CartItem> items, String lineSeparator) {
    String result = "";
    float finalPrice = 0;
    for (CartItem i : items) {{
      result += i.getItemId() + " (" + i.getMontage() + ") " + i.getPrice() + "р * "
          + i.getAmount() + "шт." + lineSeparator;
      finalPrice += i.getAmount() * i.getPrice();
    }}
    return result + lineSeparator + "итоговая цена: " + finalPrice + "р.";
  }

  private byte[] generateCheque(List<CartItem> items, List<Engine> engines) throws Exception {
    final int amount = items.size();
    String path = amount > 5 ? "cheque5.xlsx" : "cheque" + amount + ".xlsx";
    InputStream file = new ClassPathResource(path).getInputStream();
    Workbook workbook = new XSSFWorkbook(file);
    Sheet sheet = workbook.getSheetAt(0);
    Row sourceRow = sheet.getRow(27);
    float totalPrice = 0;
    for (int i = 0; i < amount; i++) {
      try {
        final String montage = items.get(i).getMontage().toLowerCase(Locale.ROOT);
        float price = montage.contains("лапы") ?engines.get(i).getPriceLapy() :
            montage.contains("комби") ? engines.get(i).getPriceCombi() :
                engines.get(i).getPriceFlanets();
        int quantity = items.get(i).getAmount();
        totalPrice += price * quantity;
        Row row = i < 5 ? sheet.getRow(25 + i) :
            xlsxWriter.copyRow(workbook, sheet, sourceRow, 25 + i);
        row.getCell(1).setCellValue(i + 1);
        row.getCell(3).setCellValue("Электродвигатель " + items.get(i).getItemId() + " " + montage);
        row.getCell(24).setCellValue(quantity);
        row.getCell(27).setCellValue("шт");
        row.getCell(29).setCellValue(price + "р.");
        row.getCell(33).setCellValue(price * amount + "р.");
      } catch (NullPointerException ignore) { }
    }
    try {
      sheet.getRow(26 + amount).getCell(37).setCellValue(totalPrice + "р.");
      sheet.getRow(27 + amount).getCell(37).setCellValue(Math.round(totalPrice * 20) / 100f + "р.");
      sheet.getRow(28 + amount).getCell(37).setCellValue(totalPrice + "р.");
    } catch (NullPointerException ignore) { }
    try {
      CellStyle style = sheet.getRow(18).getCell(8).getCellStyle();
      sheet.getRow(20).getCell(8).setCellStyle(style);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    /* try {
      sheet.getRow(29 + items.size()).getCell(1).setCellValue(
          "Всего наименований " + items.size() + ", на сумму " + totalPrice + " руб.");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (items.size() > 4) {
      try {
        sheet.addMergedRegion(
            new CellRangeAddress(49 + items.size() - 4, 49 + items.size() - 4, 1, 37));
        sheet.addMergedRegion(
            new CellRangeAddress(50 + items.size() - 4, 50 + items.size() - 4, 1, 37));
        sheet.addMergedRegion(
            new CellRangeAddress(52 + items.size() - 4, 52 + items.size() - 4, 1, 37));
      } catch (Exception ignore) {
      }
    } */
    return xlsxWriter.convertToPDF(workbook);
  }
}
