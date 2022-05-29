package excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.apache.poi.util.SAXHelper;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
/**
 * <pre>
 * 출처 : https://javavoa-mok.tistory.com/58
 * </pre>
 *
 * @author hermeswing
 */
public class ExcelSheetHandler implements XSSFBSheetHandler.SheetContentsHandler {

    private int                currentCol = -1;
    private int                currRowNum = 0;
    private static int         sheetNum = 0;

    private List<List<String>> rows       = new ArrayList<List<String>>();    //실제 엑셀을 파싱해서 담아지는 데이터
    private List<String>       row        = new ArrayList<String>();
    private List<String>       headRow     = new ArrayList<String>();
    private List<String>       header    = new ArrayList<String>(); // 타이틀

    // 내가 추가.....
    private static List<List<List<String>>> dataSheets = new ArrayList<List<List<String>>>();



    public static ExcelSheetHandler readExcel( File file ) throws Exception {

        ExcelSheetHandler sheetHandler = new ExcelSheetHandler();

        try {

            //org.apache.poi.openxml4j.opc.OPCPackage
            OPCPackage                 opc         = OPCPackage.open( file );

            //org.apache.poi.xssf.eventusermodel.XSSFReader
            XSSFReader                 xssfReader  = new XSSFReader( opc );

            //org.apache.poi.xssf.model.StylesTable
            StylesTable                styles      = xssfReader.getStylesTable();

            //org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable
            ReadOnlySharedStringsTable strings     = new ReadOnlySharedStringsTable( opc );

//            //엑셀의 시트를 하나만 가져오기입니다.
//            //여러개일경우 while문으로 추출하셔야 됩니다.
//            InputStream                inputStream = xssfReader.getSheetsData().next();
//
//
//            //org.xml.sax.InputSource
//            InputSource                inputSource = new InputSource( inputStream );
//
//            //org.xml.sax.Contenthandler
//            ContentHandler             handle      = new XSSFSheetXMLHandler( styles, strings, sheetHandler, false );
//
//            XMLReader                  xmlReader   = SAXHelper.newXMLReader();
//
//            xmlReader.setContentHandler( handle );
//
//            xmlReader.parse( inputSource );
//
//            inputStream.close();
//            opc.close();


            XMLReader xmlReader = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheetHandler, false);
            xmlReader.setContentHandler(handler);
            Iterator<InputStream> sheets = xssfReader.getSheetsData();

            List<InputSource> dataSheets = new ArrayList<InputSource>();

            // 모든 sheet의 데이터를 읽어오기
            while (sheets.hasNext()) {
                try (InputStream sheet = sheets.next()) {
                    ++sheetNum;
                    InputSource sheetSource = new InputSource(sheet);
                    xmlReader.parse(sheetSource);
                    dataSheets.add(sheetSource);
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return sheetHandler;

    }//readExcel - end

    public List<String> getRow(int i) {
        List<String> row = new ArrayList<>();
        row = rows.get(i);
        return row;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public List<String> getHeader() {
        return header;
    }

    public int getMaxRow() {
        return headRow.size();
    }

    public int getSheetNum() {
        return sheetNum;
    }

    @Override
    public void startRow( int arg0 ) {
        this.currentCol = -1;
        this.currRowNum = arg0;
    }

    @Override
    public void cell( String columnName, String value, XSSFComment var3 ) {
        int iCol     = ( new CellReference( columnName ) ).getCol();
        int emptyCol = iCol - currentCol - 1;

        for ( int i = 0; i < emptyCol; i++ ) {
            row.add( "" );
        }
        currentCol = iCol;
        row.add( value );
    }

    @Override
    public void headerFooter( String arg0, boolean arg1, String arg2 ) {
        //사용안합니다.
    }

    @Override
    public void endRow( int rowNum ) {
        if ( rowNum == 0 ) {
            headRow = new ArrayList( row );

            for (int i = 0 ; i < row.size(); i++) {
                header.add(row.get(i));
            }
        } else {
            if ( row.size() < headRow.size() ) {
                for ( int i = row.size(); i < headRow.size(); i++ ) {
                    row.add( "" );
                }
            }
            rows.add( new ArrayList( row ) );
        }
        row.clear();
    }

    @Override
    public void hyperlinkCell( String arg0, String arg1, String arg2, String arg3, XSSFComment arg4 ) {
        // TODO Auto-generated method stub

    }

}