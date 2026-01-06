package it.lmarconi.util;

import it.lmarconi.model.CardTransaction;
import it.lmarconi.model.CardTransactionMonthlySum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class ExcelUtils {

    private ExcelUtils() {
        //Empty private constructor
    }

    public static List<CardTransaction> extractTransactionsFromExcel(String excelFilePath) {
        try (FileInputStream excel = new FileInputStream(new File(excelFilePath))) {
            log.info("Trying to retrieve card transaction list from excel file {}", excelFilePath);
            return extractTransactionsFromXssfWorkbook(new XSSFWorkbook(excel));
        } catch (Exception e) {
            log.error("There was an error while trying to read an excel file with path {}", excelFilePath);
            throw new RuntimeException(e);
        }
    }

    public static List<CardTransaction> extractTransactionsFromXssfWorkbook(XSSFWorkbook workbook) {
        List<CardTransaction> transactions = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        //i starts from 3 because the first three lines of the transaction excel contains an image and the header
        for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            int j = row.getFirstCellNum();

            CardTransaction transaction = new CardTransaction();
            transaction.setEffectiveCreditDate(fromStringToDate(dataFormatter.formatCellValue(row.getCell(j))));
            transaction.setTransactionDate(fromStringToDate(dataFormatter.formatCellValue(row.getCell(++j))));
            transaction.setAmount(BigDecimal.valueOf(row.getCell(++j).getNumericCellValue()));
            transaction.setDescription(dataFormatter.formatCellValue(row.getCell(++j)));
            transactions.add(transaction);
        }
        log.info("Retrieved {} transactions from excel file", transactions.size());
        return transactions;
    }

    public static void createAndPopulateExcelReport(List<CardTransactionMonthlySum> monthlySums) {
        log.info("Preparing to create excel report");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        Map<String, Object[]> data = new TreeMap<>();
        data.put(String.valueOf(1), new Object[]{"Month", "Net Income"});
        int i = 2;
        for (CardTransactionMonthlySum monthlySum : monthlySums) {
            data.put(String.valueOf(i), new Object[]{monthlySum.getMonth(), monthlySum.getMonthlyNetIncome()});
            i++;
        }
        int rowNum = 0;
        for (String key : data.keySet()) {
            Row row = sheet.createRow(rowNum++);
            Object[] objects = data.get(key);
            int cellNum = 0;
            for (Object obj : objects) {
                Cell cell = row.createCell(cellNum++);
                switch (obj) {
                    case String str -> cell.setCellValue(str);
                    case BigDecimal bigDecimal -> cell.setCellValue(bigDecimal.doubleValue());
                    default -> cell.setCellValue(String.valueOf(obj));
                }
            }
        }

        try (FileOutputStream file = new FileOutputStream("INSERT OUTPUT PATH HERE")) {
            log.info("Attempting to create report excel file");
            workbook.write(file);
        } catch (IOException e) {
            log.error("There was an error while trying to create report excel file");
            throw new RuntimeException(e);
        }
    }

    private static LocalDate fromStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

}
