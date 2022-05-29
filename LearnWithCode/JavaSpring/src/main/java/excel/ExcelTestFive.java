package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.util.List;

import static excel.ExcelUtil.createSXSSFWorkbook;

public class ExcelTestFive {

    String megafile = "C:/Users/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/megafile.xlsx";
    String babyfile = "C:/Users/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/babyfile.xlsx";
    String newfile = "C:/Users/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/newfile.xlsx";
    String orgfile = "C:/Users/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/orgfile.xlsx";


    //.xlsx 확장자 지원
//    XSSFWorkbook workbook = null; // .xlsx
//    XSSFSheet sheet = null; // .xlsx
//    XSSFRow row = null; // .xlsx
//    XSSFCell cell = null;// .xlsx

    // SXSS
     SXSSFWorkbook workbook = null;
     SXSSFSheet sheet = null;
     SXSSFRow row = null;
     SXSSFCell cell = null;

    CellStyle style = null;

    public void createSXSSFTestFile() {

        File file = new File(orgfile);
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
                for (int i = 0; i < 10; i++) {
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


    public void readAndCopyToNewFile() throws Exception {

        File file = new File(megafile);

        // 읽을 때 XSSF-SAX
//        FileInputStream fis = new FileInputStream(file);
//        XSSFWorkbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheetAt(0);

        // 기존 파일
        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);
        List<List<String>> datas = excelSheetHandler.getRows();

        // 새로 만들 파일
        SXSSFWorkbook sxssfWorkbook = createSXSSFWorkbook(9, 500, true, false);

        Sheet sxSheet = sxssfWorkbook.createSheet("sheet");
//        int sheetNum = workbook.getNumberOfSheets();

        int rowCnt = -1;
        int cellCnt = -1;

        // 새 파일에 데이터 복사
        for(List<String> row : datas){
            rowCnt++;
            cellCnt = -1;

            Row sxSheetRow = sxSheet.createRow(rowCnt);

            for(String cell : row){ // row 하나를 읽어온다.
                cellCnt++;
//                System.out.println(cell); // cell 하나를 읽어온다.

                Cell sxCell = sxSheetRow.createCell(cellCnt);

                if (cell.equals("find me")) {
                    sxCell.setCellValue("BOOYA");
                }else {
                    sxCell.setCellValue(cell);
                }
            }
        }

//        for (int k = 0; k < sheetNum; k++) {
//
//            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
//                Row row = sheet.getRow(i);
//                Row shrow = sxSheet.createRow(i);
//
//                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
//                    Cell cell = row.getCell(j);
//                    Cell sxcell = shrow.createCell(j);
//
//                    if (cell.toString().equals("aaa")) {
//                        sxcell.setCellValue("123");
//                    } else {
//                        sxcell.setCellValue(cell.toString());
//                    }
//                }
//            }
//        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(newfile);
            sxssfWorkbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
            workbook.close();
            sxssfWorkbook.close();
            sxssfWorkbook.dispose();
        }

    }


//    public void finalRound() throws Exception {
//
//        File file = new File(megafile);
//        InputStream is = new FileInputStream(file);
//        StreamingReader reader= StreamingReader.builder()
//                .rowCacheSize(100)
//                .bufferSize(4096)
//                .sheetIndex(0)
//                .read(is);
//
//        // 새로 만들 파일
//        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
//        Sheet sxSheet = sxssfWorkbook.createSheet("sheet");
////        int sheetNum = workbook.getNumberOfSheets();
//
//
//        int rowCnt = -1;
//        int cellCnt = -1;
//
//        // 새 파일에 데이터 복사
//        for(Row row : reader){
//            rowCnt++;
//            cellCnt = -1;
//
//            Row sxSheetRow = sxSheet.createRow(rowCnt);
//
//            for(Cell cell : row){ // row 하나를 읽어온다.
//                cellCnt++;
////                System.out.println(cell); // cell 하나를 읽어온다.
//
//                Cell sxCell = sxSheetRow.createCell(cellCnt);
//
//                if (cell.toString().equals("find me")) {
//                    sxCell.setCellValue("BOOYA");
//                } else {
//                    sxCell.setCellValue(cell.toString());
//                }
//            }
//        }
//
//    }
}
