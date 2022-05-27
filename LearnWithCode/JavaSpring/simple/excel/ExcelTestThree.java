package simple.excel;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelTestThree {

    String megafile = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/megafile.xlsx";
    String babyfile = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/babyfile.xlsx";

    //.xlsx 확장자 지원
    SXSSFWorkbook workbook = null; // .xlsx
    SXSSFSheet sheet = null; // .xlsx
    SXSSFRow row = null; // .xlsx
    SXSSFCell cell = null;// .xlsx

    // SXSS
    // SXSSFWorkbook workbook = null;
    // SXSSFSheet sheet = null;
    // SXSSFRow row = null;
    // SXSSFCell cell = null;

    CellStyle style = null;




    // 파일 복사 - 기존에 복사된 파일이 존재하면 덮어쓰기
//            Files.copy(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    /** 파일생성, 수정 */
    public void startFromTheBottom() {

        try {
            int rowNo = -1; // 행 갯수
            int colNo = -1;

            // 워크북 생성
            workbook = new SXSSFWorkbook();
            sheet = workbook.createSheet("엑셀 테스트"); // 워크시트 이름

            style = workbook.createCellStyle();

            //헤더 생성
            row = sheet.createRow(++rowNo); //헤더 01
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);

            row = sheet.createRow(++rowNo);
            colNo = -1;
            cell = row.createCell(++colNo);
            cell.setCellValue("bbbb");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("bbbb");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("bbbb");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("bbbb");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("bbbb");
            cell.setCellStyle(style);

            row = sheet.createRow(++rowNo);
            colNo = -1;
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);

            row = sheet.createRow(++rowNo);
            colNo = -1;
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);

            row = sheet.createRow(++rowNo);
            colNo = -1;
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);
            cell = row.createCell(++colNo);
            cell.setCellValue("aaaaa");
            cell.setCellStyle(style);

            File file = new File(babyfile);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            workbook.write(fos);

            if (workbook != null)	workbook.close();
            if (fos != null) fos.close();

            workbook.dispose();

        }
        catch(Exception e){

        }finally{

        }

    }

    /**
     * 메인 테스트
     */
    public void testWriteFile() {

        // 파일 복사본 만들기
//    if(!luxDivcList.isEmpty()) {
        try {

            FileInputStream fis = new FileInputStream(megafile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook);

            Font font = workbook.createFont();
            font.setFontName("Gulim");
            style.setFont(font);

            // 시트별로 반복
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(0);

//                // 로우를 하나하나 확인
                for (int j = 0; j < 3; j++) {
                    Cell cell = sheet.getRow(j).getCell(1); // B열만 훑기
                    cell.setCellValue("TROLL");
                }
            }

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(megafile);
                sxssfWorkbook.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(workbook);
                IOUtils.closeQuietly(fos);
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to renew excel file " + e.getMessage());
        }
    }


    /** 30메가짜리 엑셀파일 만들기 */
    public void createMegaFile() {
        File file = new File(megafile);
        FileOutputStream fos = null;

        try {
            // 워크북 생성
            workbook = new SXSSFWorkbook();

            Font font = workbook.createFont();
            font.setFontName("Gulim");
            style = workbook.createCellStyle();
            style.setFont(font);

            for(int k = 0; k < 3; k++) {
                sheet = workbook.createSheet("waneemade" + k); // 워크시트 이름
                for (int i = 0; i < 150000; i++) {
                    row = sheet.createRow(i);
                    for (int j = 0; j < 30; j++) {
                        cell = row.createCell(j);
                        cell.setCellValue("aaa");
                        cell.setCellStyle(style);
                    }
                }
            }

            fos = new FileOutputStream(file);
            workbook.write(fos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        IOUtils.closeQuietly(workbook);
        IOUtils.closeQuietly(fos);

        }

    }

    public void readMegaFile() throws Exception {
        //해당 파일은 업로드파일
        File file = new File(megafile);

        long start = System.currentTimeMillis(); //시작하는 시점 계산

        ExcelSheetHandler  excelSheetHandler = ExcelSheetHandler.readExcel( file );
        List<List<String>> excelDatas        = excelSheetHandler.getRows();

        long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산

        System.out.println( "실행 시간 : " + ( end - start ) / 1000.0 + "초" ); //실행 시간 계산 및 출력

    }


}
