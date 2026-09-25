package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

public final class ExcelUtils {

    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    public static Object[][] getTestData(String relativePath, String sheetName) {
        DataFormatter formatter = new DataFormatter();
        List<Object[]> records = new ArrayList<>();

        try (InputStream is = getFileStream(relativePath);
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in {}", sheetName, relativePath);
                throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist.");
            }

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                logger.warn("Sheet '{}' contains no data rows (only header or empty).", sheetName);
                return new Object[0][0];
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warn("Sheet '{}' header row is null.", sheetName);
                return new Object[0][0];
            }

            int totalCols = headerRow.getLastCellNum();

            // Iterate over all data rows starting from row index 1
            for (int r = 1; r <= lastRowNum; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue; // Skip physically empty row
                }

                String[] rowData = new String[totalCols];
                boolean isRowEmpty = true;

                for (int c = 0; c < totalCols; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = formatter.formatCellValue(cell).trim();
                    rowData[c] = cellValue;
                    if (!cellValue.isEmpty()) {
                        isRowEmpty = false;
                    }
                }

                // Only add rows that contain actual values
                if (!isRowEmpty) {
                    records.add(rowData);
                }
            }

            logger.info("Successfully loaded {} data rows from sheet '{}'", records.size(), sheetName);

        } catch (Exception e) {
            logger.error("Error reading test data from {}: {}", relativePath, e.getMessage(), e);
            throw new RuntimeException("Failed to read Excel data: " + e.getMessage(), e);
        }

        return records.toArray(new Object[0][]);
    }

    /**
     * Resolves the input stream via ClassLoader first, with a fallback to direct filesystem path.
     */
    private static InputStream getFileStream(String path) throws Exception {
        // 1. Try ClassLoader (standard for src/test/resources)
        InputStream stream = ExcelUtils.class.getClassLoader().getResourceAsStream(path);
        if (stream != null) {
            return stream;
        }

        // 2. Fallback: Check direct project root path
        File file = new File(System.getProperty("user.dir") + File.separator + "src" 
                + File.separator + "test" + File.separator + "resources" + File.separator + path);
        if (file.exists()) {
            return new FileInputStream(file);
        }

        throw new IllegalArgumentException("Excel file not found at classpath or path: " + path);
    }
}