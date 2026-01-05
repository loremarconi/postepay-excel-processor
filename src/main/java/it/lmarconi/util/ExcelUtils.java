package it.lmarconi.util;

import it.lmarconi.model.CardTransaction;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

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

    private static LocalDate fromStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

}
