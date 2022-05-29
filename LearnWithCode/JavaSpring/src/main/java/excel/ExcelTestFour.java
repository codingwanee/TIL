package excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.util.List;

public class ExcelTestFour {


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

//
//    public void readMegafileUsingSAXParser () throws OpenXML4JException, IOException, SAXException {
//
//        // 엑셀파일 저장할 List
//        List<String[]> dataList = null;
//
//
//        OPCPackage opc = null;
//
//        opc = OPCPackage.open(megafile);
//        XSSFReader xssfReader = null;
//
//        xssfReader = new XSSFReader(opc);
//        XSSFReader.SheetIterator itr = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
//
//        StylesTable styles = xssfReader.getStylesTable();
//
//        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
//        dataList = new ArrayList<String[]>();
//
//        while (itr.hasNext()) {
//            InputStream sheetStream = itr.next();
//            InputSource sheetSource = new InputSource(sheetStream);
//
//        }
//
//
//        XSSFSheetXMLHandler handler = new XSSFSheetXMLHandler();
//
//        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//        File xmlFile = new File(megafile);
//        parser.parse(xmlFile, new DefaultHandler());
//
//
//    }
//


    public void trial() throws Exception {

        File file = new File(babyfile);

        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);
        List<List<String>> datas = excelSheetHandler.getRows();

        for(List<String> row : datas){
            for(String cell : row){ // row 하나를 읽어온다.
                System.out.println(cell); // cell 하나를 읽어온다.
            }
        }

    }
}
