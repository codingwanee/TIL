package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**************************************************************************
 * 넥사크로 엑셀다운로드 공통함수 사용 시 메모리 부족 에러 발생
 * ∴ 대용량데이터는 해당 엑셀함수 사용.
 **************************************************************************/


public class ExcelUtils {
    private Sheet sheet;
    private int rowIndex = 0;
    private int maxCols = 0;
    private Workbook workbook;
    private int offsetCol = 0;


    /**
     * 생성자
     * @param sheetName 시트명
     */
    public ExcelUtils(String sheetName) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }


    /**
     * 생성자
     * @param sheetName 시트명
     * @param rowAccessWindowSize flushRows단위
     *        * 해당 값이 NULL일경우 XSSF를 , 없을 경우 SXSSF 대용량 라이브러리를 사용
     *        * XSSF : 엑셀 2007부터 지원하는 OOXML 파일 포맷인 *.xlsx 파일을 읽고 쓰는 컴포넌트(데이터를 메모리에 담아서 한번에 처리) => 메모리 부족현상 발생
     *        * SXSSF : SXSSF는 자동으로 메모리에 일정량의 데이터가 차면 메모리를 비워줌. *.xlsx 파일 생성
     */
    public ExcelUtils(String sheetName, Integer rowAccessWindowSize) {
        if (rowAccessWindowSize != null) {
            workbook = new SXSSFWorkbook(rowAccessWindowSize);// row 단위 flush
            sheet = workbook.createSheet(sheetName);
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet(sheetName);
        }
    }

    /**
     * 엑셀의 시작점을 설정한다.
     * @param col 엑셀시작컬럼위치
     * @param row 엑셀시작로우위치
     */
    public void setOffset(int col, int row) {
        offsetCol = col;
        rowIndex = row;
    }

    /**
     * 엑셀에 ROW를 추가한다.
     * @param rows 엑셀에 보여줄 ROW데이터
     */
    public <T> void addRow(List<T> rows) {
        Row header = sheet.createRow(rowIndex++);
        int cellIndex = offsetCol;
        for (T value : rows) {
            Cell cell = header.createCell(cellIndex++);
            cell.setCellValue(String.valueOf(value));
        }
        if (maxCols < cellIndex) {
            maxCols = cellIndex;
        }
    }

    /**
     * 엑셀에 ROW를 추가한다.
     * @param rsExcel 외부에서 생성한 ExcelUtils 객체
     * @param header  헤더에 보여줄 ROW 데이터
     * @param list    본문에 보여줄 ROW데이터
     */
    public void write(ExcelUtils rsExcel, List<?> header, List<?> list, String filepath) throws IOException {
        //파일명설정 = > 설정이름_년월일시분초.xlsx
//        String fName = fileName  + "_" + Utils.getTodayTime("yyyyMMddHHmmss") + ".xlsx";
//        String filePath = url + "Excel/";

        //엑셀 시작점 설정
        rsExcel.setOffset(0, 0);

        //엑셀 헤더 추가
        rsExcel.addRow(header);

        //List<특정Obejct> 형태를 List<String> 형태로 변환한 뒤 데이터를 그린다.
//        for (int i = 0; i < list.size(); i++) {
//            rsExcel.addRow(Utils.ConverObjectToStringList(list.get(i)));
//        }

        //실제 파일로 생성
        FileOutputStream fos = new FileOutputStream(filepath);

        this.sheet.getWorkbook().write(fos);
        if (this.workbook instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) this.workbook).dispose();
        }

        this.workbook.close();


    }
}