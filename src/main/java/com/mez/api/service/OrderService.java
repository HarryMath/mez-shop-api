package com.mez.api.service;

import com.mez.api.models.DTO.CartItem;
import com.mez.api.models.Engine;
import com.mez.api.models.Order;
import com.mez.api.repository.EngineRepository;
import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.XlsxWriter;
import com.mez.api.tools.bots.MailBot;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class OrderService {

  private final MailBot mailBot;
  private final XlsxWriter xlsxWriter;
  private final EngineRepository engineRepository;

  @Autowired
  OrderService(MailBot mailBot, XlsxWriter xlsxWriter,
      EngineRepository engineRepository) {
    this.mailBot = mailBot;
    this.xlsxWriter = xlsxWriter;
    this.engineRepository = engineRepository;
  }

  public String sendCheque(Order order) {
    try {
      List<CartItem> items = order.getItems();
      List<Engine> engines = engineRepository.getEngines(items);
      if (engines.size() != items.size()) {
//        return ResponseCodes.DATABASE_ERROR;
        return "DATA_ERROR";
      }
      final int amount = items.size();
      String path = amount > 3 ? "cheque.xlsx" : "cheque" + amount + ".xlsx";
      InputStream file = new ClassPathResource(path).getInputStream();
      Workbook workbook = new XSSFWorkbook(file);
      Sheet sheet = workbook.getSheetAt(0);
      Row sourceRow = sheet.getRow(27);
      float totalPrice = 0;
      for (int i = 0; i < amount; i++) {
        try {
          float price = engines.get(i).getPrice();
          int quantity = items.get(i).getAmount();
          totalPrice += price * quantity;
          Row row = i < 4 ? sheet.getRow(25 + i) :
              xlsxWriter.copyRow(workbook, sheet, sourceRow, 25 + i);
          row.getCell(1).setCellValue(i + 1);
          row.getCell(7).setCellValue("Электродвигатель " + items.get(i).getItemId());
          row.getCell(24).setCellValue(quantity);
          row.getCell(27).setCellValue("шт");
          row.getCell(29).setCellValue(price);
          row.getCell(33).setCellValue(price * amount);
        } catch (NullPointerException ignore) { }
      }
      try {
        sheet.getRow(26 + amount).getCell(33).setCellValue(totalPrice);
        sheet.getRow(27 + amount).getCell(33).setCellValue(0.0);
        sheet.getRow(28 + amount).getCell(33).setCellValue(totalPrice);
      } catch (NullPointerException ignore) { }
      try {
        CellStyle style = sheet.getRow(18).getCell(8).getCellStyle();
        sheet.getRow(20).getCell(8).setCellValue(order.getName());
        sheet.getRow(20).getCell(8).setCellStyle(style);
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      try {
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
      }
      byte[] document = xlsxWriter.convertToPDF(workbook);
      return mailBot.send(order.getMail(), "Заказ в магазине mez", "Здравствуйте...", document, "чек.pdf");
//      if (mailBot.send(order.getMail(), "Заказ в магазине mez", "Здравствуйте...", document, "чек.pdf")) {
//        return ResponseCodes.SUCCESS;
//      } else {
//        System.out.println("message not sent");
//        return ResponseCodes.UNKNOWN_ERROR;
//      }
    } catch (Exception e) {
      return "ERROR creating cheque: " + e.getMessage();
    }
  }
}
