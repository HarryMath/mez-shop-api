package com.mez.api.tools.excell;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XlsxWriter {

  @Value("${cloudmersive.key}")
  private String apiKey;

  public Row copyRow(Workbook workbook, Sheet sheet, Row sourceRow, int destinationRowOrder) {
    Row destinationRow = sheet.getRow(destinationRowOrder);
    // If the row exist in destination, push down all rows by 1 else create a new row
    sheet.shiftRows(destinationRowOrder + 1, sheet.getLastRowNum(), 1, true, true);
    destinationRow = sheet.getRow(destinationRowOrder);
    if (destinationRow == null) {
      destinationRow = sheet.createRow(destinationRowOrder);
    }
    destinationRow.setHeight(sourceRow.getHeight());
    // Loop through source columns to add to new row
    for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
      // Grab a copy of the old/new cell
      Cell oldCell = sourceRow.getCell(i);
      Cell newCell = destinationRow.createCell(i);

      // If the old cell is null jump to next cell
      if (oldCell == null) {
        continue;
      }

      // Copy style from old cell and apply to new cell
      CellStyle newCellStyle = workbook.createCellStyle();
      newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
      newCell.setCellStyle(newCellStyle);

      // If there is a cell hyperlink, copy
      if (oldCell.getHyperlink() != null) {
        newCell.setHyperlink(oldCell.getHyperlink());
      }

      // Set the cell data value
      switch (oldCell.getCellType()) {
        case Cell.CELL_TYPE_BLANK:
          newCell.setCellValue(oldCell.getStringCellValue());
          break;
        case Cell.CELL_TYPE_BOOLEAN:
          newCell.setCellValue(oldCell.getBooleanCellValue());
          break;
        case Cell.CELL_TYPE_ERROR:
          newCell.setCellErrorValue(oldCell.getErrorCellValue());
          break;
        case Cell.CELL_TYPE_FORMULA:
          newCell.setCellFormula(oldCell.getCellFormula());
          break;
        case Cell.CELL_TYPE_NUMERIC:
          newCell.setCellValue(oldCell.getNumericCellValue());
          break;
        case Cell.CELL_TYPE_STRING:
          newCell.setCellValue(oldCell.getRichStringCellValue());
          break;
      }
    }
    // If there are are any merged regions in the source row, copy to new row
    for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
      try {
        CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
        if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
          CellRangeAddress newCellRangeAddress = new CellRangeAddress(destinationRow.getRowNum(),
              (destinationRow.getRowNum() +
                  (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                  )),
              cellRangeAddress.getFirstColumn(),
              cellRangeAddress.getLastColumn());
          sheet.addMergedRegion(newCellRangeAddress);
        }
      } catch (Exception ignore) {}
    }
    return destinationRow;
  }

  public byte[] convertToPDF(Workbook workbook) throws ApiException, IOException {
    OutputStream stream = new FileOutputStream("saved.xlsx");
    workbook.write(stream);
    workbook.close();
    File file = new File("saved.xlsx");

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
    Apikey.setApiKey(apiKey);
    ConvertDocumentApi apiInstance = new ConvertDocumentApi();
    byte[] result = apiInstance.convertDocumentXlsxToPdf(file);
    file.delete();
    return result;
  }
}
