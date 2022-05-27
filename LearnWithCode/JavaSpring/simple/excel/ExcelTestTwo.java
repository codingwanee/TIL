package simple.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class ExcelTestTwo {

    public void fileMethod () {
        // 파일 복사본 만들기
        try {

            File testfile = new File("/Users/wanee/Documents/study/JavaSpring/src/main/resources/TestExcel.xlsx");

            FileInputStream fis = new FileInputStream(testfile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);


            // sheet 개수 가져오기 - 모든 sheet 조회
            int sheets = workbook.getNumberOfSheets();

            // 시트별로 반복
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // dao별로 반복
//                for (DmtDivcDao dao : daoList) {
//                    String divcNo = dao.getDivcNo();

                    // 로우를 하나하나 확인
                    for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                        System.out.println("############## per row ");

                        Cell cell = sheet.getRow(j).getCell(1); // B열만 훑기
                        String cellVal = cell.toString();

                        System.out.println("#### cellVal : " + cellVal);

//                        cell.setCellValue("memememe");
//                        System.out.println("#### is it changed : " + cell);

                        // 특정 값 찾기
                        if (cellVal.equals("b22")) {
                            cell.setCellValue("here i am");
                            System.out.println("#### is it changed : " + cell);
                        }
//                    }
                }
            }

            // TODO 최종 저장
//            try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
//                log.info("#### trying to save file");
//
//                workbook.write(fileOutputStream); // 덮어쓰기 or 새로만들기
//                workbook.close();
//
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(testfile);
                workbook.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(workbook);
                IOUtils.closeQuietly(fos);
            }

        } catch (
                IOException e) {
            throw new RuntimeException("fail to renew excel file " + e.getMessage());
        }

    }

}
