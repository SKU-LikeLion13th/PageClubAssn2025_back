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
        List<Map<String, Object>> result = new ArrayList<>(); // 데이터 저장
        InputStream inputStream = file.getInputStream(); // 파일의 입력 스트림 가져오기
        Workbook workbook = new XSSFWorkbook(inputStream); // 확장자가 .xlsx인 파일을 읽어 workbook 객체 생성
        Sheet sheet = workbook.getSheetAt(0); // 첫번째 시트 가져오기

        // 블록 단위로 데이터 읽는 루프
        for (int startRow = 3; startRow <= sheet.getLastRowNum(); startRow += 31) { // 3행부터 시작, 매 31행마다 새로운 데이터 블록이 존재함
            String clubName = getMergedCellValue(sheet, startRow, 1); // 병합된 셀에서 동아리명 읽어옴, 동아리명은 인덱스 기준으로 1, 즉 B열에 있음

            // 열 그룹별(학번-이름)로 데이터 읽는 루프
            for (int columnGroup = 3; columnGroup <= 9; columnGroup += 3) { // 열 그룹(학번-이름): D-E, G-H, J-K
                // 행 단위로 데이터 읽는 루프
                for (int rowIndex = startRow; rowIndex < startRow + 30 && rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                    // 빈 행 처리 안 함
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue; // 전체가 비어있는 행 처리 안 함

                    String studentId = getCellValue(row.getCell(columnGroup)); // 학번 추출 (D, G, J열)
                    String studentName = getCellValue(row.getCell(columnGroup + 1)); // 이름 추출 (E, H, K열)

                    if (!studentId.isEmpty() && !studentName.isEmpty()) { // 학번과 이름이 비어 있지 않은 경우만 처리 (행 객체는 존재하지만 셀 값이 비어있는 데이터 필터링)
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

    // 병합된 셀 값 가져오기
    private static String getMergedCellValue(Sheet sheet, int row, int column) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) { // 병합된 영역 순회 // sheet.getNumMergedRegions() -> 병합된 영역의 개수 반환
            CellRangeAddress region = sheet.getMergedRegion(i); // 병합된 셀 영역 중 i번째 영역에 대한 정보
            if (region.isInRange(row, column)) { // 특정 셀이 병합된 영역 안에 있는지 확인
                // 병합된 영역의 첫 번째 행, 첫 번째 열에서 데이터 읽기 // 병합된 영역에서는 데이터가 항상 첫 번째 셀에 저장되므로...
                Row mergedRow = sheet.getRow(region.getFirstRow());
                Cell mergedCell = mergedRow.getCell(region.getFirstColumn());
                return getCellValue(mergedCell);
            }
        }
        return "";
    }

    // 셀 값 읽기
    private static String getCellValue(Cell cell) {
        if (cell == null) return ""; // 셀이 비었으면 빈 문자열 반환
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue()); // 숫자를 long으로 변환 후 String으로 변환
            default -> "";
        };
    }
}
