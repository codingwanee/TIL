package simple.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Iterator;

public class ExcelFileTest {

    String babyfile = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/babyfile.xlsx";

    public ExcelFileTest() throws URISyntaxException, IOException, FileNotFoundException {

        File file = new File(babyfile);

        // 파일 가져오기 (테스트용)
        FileInputStream filein = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(filein);

        // TODO sheet 개수 식별하기
        int sheets = workbook.getNumberOfSheets();

        // 시트별로 반복
        for (int i = 0 ; i < sheets ; i ++) {
            Sheet sheet = workbook.getSheetAt(i);
            Iterator<Row> rowItr = sheet.iterator();

            // 행이 있는 동안
//            while (rowItr.hasNext()) {
//                Row row = rowItr.next();
//                System.out.println("#### contents : " + row.getCell(0).toString());
//            }

            // 로우를 하나하나 확인
            for (int j = 0 ; j < sheet.getPhysicalNumberOfRows(); j ++ ) {
                Cell cell = sheet.getRow(j).getCell(1); // B열만 훑기
                String cellVal = cell.toString();

                // 특정 값 찾기
                if (cellVal.equals("b5")) {
                    cell.setCellValue("whyyyy");
                } else {
                }
            }
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(fos);
        }
    }

    private void writeFile(XSSFWorkbook workbook, File file) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(fos);
        }
    }
}