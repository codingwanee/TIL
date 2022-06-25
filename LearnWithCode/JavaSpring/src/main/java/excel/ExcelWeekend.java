package excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static excel.ExcelUtil.createSXSSFWorkbook;

@Slf4j
public class ExcelWeekend {

    String megafile = "C:/Users/hwjun/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/megafile.xlsx";
    String babyfile = "C:/Users/hwjun/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/babyfile.xlsx";
    String newfile = "C:/Users/hwjun/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/newfile.xlsx";
    String orgfile = "C:/Users/hwjun/workspace/workspace-TIL/TIL-General/LearnWithCode/JavaSpring/src/main/resources/orgfile.xlsx";


    // SXSS
    SXSSFWorkbook workbook = null;
    SXSSFSheet sheet = null;
    SXSSFRow row = null;
    SXSSFCell cell = null;

    CellStyle style = null;


    /**
     * 파일내용 복사해서 새 파일에 저장
     */
    public void readAndCopyToNewFile() throws Exception {

        File file = new File(megafile);

        // 기존 파일
        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);
        List<List<String>> datas = excelSheetHandler.getRows();

        // 새로 만들 파일
        SXSSFWorkbook sxssfWorkbook = createSXSSFWorkbook(9, 500, true, false);

        // Sheet sxSheet = sxssfWorkbook.createSheet("sheet");
        // int sheetNum = workbook.getNumberOfSheets();

        int totalRowNum = datas.size();
        int totalSheetNum = (totalRowNum / 25) + 1;

        System.out.println("size : " + totalRowNum);

//        for (int i = 0; i < totalSheetNum; i++) {
//            SXSSFSheet sxssfSheet = sxssfWorkbook.createSheet("sheet" + i);
//        }
//        List<List<List<String>>> master_data = null;
//        List<List<String>> pack = null;
//        for (int j = 0; j < totalRowNum; j++) { // 450000
//            List<String> data = datas.get(j);
//            pack.add(data);
//            if (j % 200000 == 0) {
//                master_data.add(pack);
//                pack.clear();
//            }
//        }

        int rowCnt = 0;
        int cellCnt = -1;

//
//        for (int i = 0 ; i < 1 ; i++) {
//
//        }

        List<List<List<String>>> choppedDatas = ExcelUtil.chopped(datas, 199999);

        // TODO sheet 개수 구하기 - 생성
        for (int k = 0; k < excelSheetHandler.getSheetNum(); k++) {

            SXSSFSheet sxSheet = sxssfWorkbook.createSheet("sheet" + k);
            Row headerRow = sxSheet.createRow(0);

            // 시트별 헤더 입력
            for (int i = 0; i < excelSheetHandler.getMaxRow(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(excelSheetHandler.getHeader().get(i));
            }

            rowCnt = 0;

            // 새 파일에 데이터 복사
            for (List<String> row : choppedDatas.get(k)) { // row를 하나씩 읽어온다
                rowCnt++;
                cellCnt = -1;

                // TODO 데이터 찾아바꾸기
                Row sxSheetRow = sxSheet.createRow(rowCnt);

                for (String cell : row) { // cell을 하나씩 읽어온다
                    cellCnt++;
                    Cell sxCell = sxSheetRow.createCell(cellCnt);

                    if (cell.equals("find me")) {
                        sxCell.setCellValue("BOOYA");
                    } else {
                        sxCell.setCellValue(cell);
                    }

                }
            }
        }


        // for (int k = 0; k < sheetNum; k++) {
//
// for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
// Row row = sheet.getRow(i);
// Row shrow = sxSheet.createRow(i);
//
// for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
// Cell cell = row.getCell(j);
// Cell sxcell = shrow.createCell(j);
//
// if (cell.toString().equals("aaa")) {
// sxcell.setCellValue("123");
// } else {
// sxcell.setCellValue(cell.toString());
// }
// }
// }
// }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(newfile);
            sxssfWorkbook.write(fos);
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
            sxssfWorkbook.close();
            sxssfWorkbook.dispose();
        }

    }


    public void ssibal() throws Exception {
        ExampleEventUserModel exampleEventUserModel = new ExampleEventUserModel();

        exampleEventUserModel.processAllSheets(megafile);


    }

}

