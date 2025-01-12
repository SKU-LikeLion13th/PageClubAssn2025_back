package likelion13.page.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReaderService {

    public static List<Map<String, Object>> readExcel(MultipartFile file) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int startRow = 3; startRow <= sheet.getLastRowNum(); startRow += 31) {
            String clubName = getMergedCellValue(sheet, startRow, 1);

            for (int rowIndex = startRow; rowIndex < startRow + 30 && rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                // Skip the header row (e.g., 34, 65, ...)
                if ((rowIndex - 3) % 31 == 30) continue;

                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                for (int columnGroup = 3; columnGroup <= 9; columnGroup += 3) {
                    String studentId = getCellValue(row.getCell(columnGroup));
                    String studentName = getCellValue(row.getCell(columnGroup + 1));

                    if (!studentId.isEmpty() && !studentName.isEmpty()) {
                        Map<String, Object> rowData = new HashMap<>();
                        rowData.put("clubName", clubName);
                        rowData.put("studentId", studentId);
                        rowData.put("studentName", studentName);
                        result.add(rowData);
                    }
                }
            }
        }

        workbook.close();
        return result;
    }

    private static String getMergedCellValue(Sheet sheet, int row, int column) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.isInRange(row, column)) {
                Row mergedRow = sheet.getRow(region.getFirstRow());
                Cell mergedCell = mergedRow.getCell(region.getFirstColumn());
                return getCellValue(mergedCell);
            }
        }
        return "";
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}