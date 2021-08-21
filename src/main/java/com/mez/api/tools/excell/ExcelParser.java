package com.mez.api.tools.excell;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.DTO.EngineUpload;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelParser {

  public List<EngineUpload> parseEngines(MultipartFile file) throws IOException {
    List<EngineUpload> result = new ArrayList<>();
    Workbook workbook;
    try {
      workbook = new XSSFWorkbook(file.getInputStream());
    } catch (Exception ignore) {
      workbook = new HSSFWorkbook(file.getInputStream());
    }
    Sheet sheet = workbook.getSheetAt(0);
    Iterator<Row> rows = sheet.rowIterator();
    int rowsCounter = 0;
    EngineUpload engine = null;
    String lastEngineName = "";
    while (rows.hasNext()) {
      Row row = rows.next();
      if (++rowsCounter > 1 && row != null && row.getCell(0) != null) {
        String firstSellValue = getStringValue(row.getCell(0));
        if (firstSellValue.equals(lastEngineName) && engine != null) {
          engine.getCharacteristics().add(parseDataRow(row));
        } else {
          if (engine != null) {
            result.add(engine);
          }
          lastEngineName = firstSellValue;
          engine = new EngineUpload();
          engine.setName(lastEngineName);
          engine.setManufacturer("ОАО «Могилевлифтмаш»");
          engine.setType(getStringValue(row.getCell(19)));
          engine.setMass(getNumericValue(row.getCell(17)));
          engine.setAxisHeight(getNumericValue(row.getCell(20)));
          engine.setPriceLapy(getNumericValue(row.getCell(21)));
          engine.setPriceCombi(getNumericValue(row.getCell(22)));
          engine.setPriceFlanets(getNumericValue(row.getCell(23)));
          engine.setCharacteristics(new ArrayList<>());
          engine.getCharacteristics().add(parseDataRow(row));
        }
      }
    }
    if (engine != null) {
      result.add(engine);
    }
    return result;
  }

  private CharacteristicsRow parseDataRow(Row row) {
    CharacteristicsRow dataRow = new CharacteristicsRow();
    dataRow.setPower(getNumericValue(row.getCell(1)));
    dataRow.setFrequency(Math.round(getNumericValue(row.getCell(2))));
    dataRow.setEfficiency(getNumericValue(row.getCell(3)));
    dataRow.setCosFi(getNumericValue(row.getCell(4)));
    dataRow.setElectricityNominal115(getNumericValue(row.getCell(5)));
    dataRow.setElectricityNominal220(getNumericValue(row.getCell(6)));
    dataRow.setElectricityNominal380(getNumericValue(row.getCell(7)));
    dataRow.setElectricityRatio(getNumericValue(row.getCell(8)));
    dataRow.setMomentsRatio(getNumericValue(row.getCell(9)));
    dataRow.setMomentsMaxRatio(getNumericValue(row.getCell(10)));
    dataRow.setMomentsMinRatio(getNumericValue(row.getCell(11)));
    dataRow.setVoltage115(getNumericValue(row.getCell(12)));
    dataRow.setVoltage220_230(getNumericValue(row.getCell(13)));
    dataRow.setCapacity115(getNumericValue(row.getCell(14)));
    dataRow.setCapacity220(getNumericValue(row.getCell(15)));
    dataRow.setCapacity230(getNumericValue(row.getCell(16)));
    dataRow.setCriticalSlipping(getNumericValue(row.getCell(18)));
    return dataRow;
  }

  private float getNumericValue(Cell cell) {
    if (cell == null) {
      return 0;
    }
    switch (cell.getCellType()) { //Identify CELL type
      case Cell.CELL_TYPE_STRING:
        try {
          return Float.parseFloat(cell.getStringCellValue());
        } catch (Exception ignore) {
          return 0;
        }
      case Cell.CELL_TYPE_NUMERIC:
        return (float) cell.getNumericCellValue();
      default:
        return 0;
    }
  }

  private String getStringValue(Cell cell) {
    if (cell == null) {
      return "0";
    }
    switch (cell.getCellType()) { //Identify CELL type
      case Cell.CELL_TYPE_STRING:
        return cell.getStringCellValue();
      case Cell.CELL_TYPE_NUMERIC:
        return String.valueOf(cell.getNumericCellValue());
      default:
        return "0";
    }
  }
}
