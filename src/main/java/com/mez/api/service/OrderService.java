package com.mez.api.service;

import com.mez.api.models.DTO.CartItem;
import com.mez.api.models.Order;
import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.XlsxWriter;
import com.mez.api.tools.bots.MailBot;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class OrderService {

  private final MailBot mailBot;
  private final XlsxWriter xlsxWriter;

  @Autowired
  OrderService(MailBot mailBot, XlsxWriter xlsxWriter) {
    this.mailBot = mailBot;
    this.xlsxWriter = xlsxWriter;
  }

  public byte sendCheque(Order order) {
    try {
      FileInputStream file = new FileInputStream(ResourceUtils.getFile("classpath:cheque.xlsx"));
      Workbook workbook = new XSSFWorkbook(file);
      Sheet sheet = workbook.getSheetAt(0);
      List<CartItem> items = order.getItems();
      Row sourceRow = sheet.getRow(27);
      for (int i = 0; i < items.size(); i++) {
        try {
          Row row = i < 4 ? sheet.getRow(25 + i) :
              xlsxWriter.copyRow(workbook, sheet, sourceRow, 25 + i);
          row.getCell(1).setCellValue(i + 1);
          row.getCell(7).setCellValue("Электродвигатель " + items.get(i).getItemId());
          row.getCell(24).setCellValue(items.get(i).getAmount());
          row.getCell(27).setCellValue("шт");
        } catch (NullPointerException e) {
          System.out.println("error in row: " + 26 + i + "; engine number " + i + "\n" + e.getMessage());
        }
      }
      try {
        CellStyle style = sheet.getRow(18).getCell(8).getCellStyle();
        sheet.getRow(20).getCell(8).setCellValue(order.getName());
        sheet.getRow(20).getCell(8).setCellStyle(style);
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      sheet.getRow(29 + items.size()).getCell(1)
          .setCellValue("Всего наименований " + items.size() + ", на сумму ??? руб.");
      try {
        sheet.addMergedRegion(new CellRangeAddress(49 + items.size() - 4, 49 + items.size() - 4, 1, 37));
        sheet.addMergedRegion(new CellRangeAddress(50 + items.size() - 4, 50 + items.size() - 4, 1, 37));
        sheet.addMergedRegion(new CellRangeAddress(52 + items.size() - 4, 52 + items.size() - 4, 1, 37));
      } catch (Exception ignore) {
        ignore.printStackTrace();
      }
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      try {
        workbook.write(stream);
      } finally {
        stream.close();
      }
      workbook.close();
      return mailBot.send(order.getMail(), "Заказ в магазине mez", "Здравствуйте...", stream.toByteArray()) ?
          ResponseCodes.SUCCESS : ResponseCodes.UNKNOWN_ERROR;
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseCodes.UNKNOWN_ERROR;
    }
  }
}
