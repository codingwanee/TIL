package simple;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;

public class SheetHandlerCaller {

    public static SheetHandler readExcel(MultipartDataSource multipart) {

        //SheetcontentHandler를 재정의 해서 만든 Class
        SheetHandler sheetHandler = new SheetHandler();

        try {

            OPCPackage pkg = OPCPackage.open(multipart.getInputStream());

            //메모리를 적게 사용하며 sax 형식을 사용할 수 있게 함
            XSSFReader xssfReader = new XSSFReader(pkg);

            //파일의 데이터를 Table형식으로 읽을 수 있도록 지원
            ReadOnlySharedStringsTable data = new ReadOnlySharedStringsTable(pkg);

            //읽어온 Table에 적용되어 있는 Style
            StylesTable styles = xssfReader.getStylesTable();

            //엑셀의 첫번째 sheet정보만 읽어오기 위해 사용 만약 다중 sheet 처리를 위해서는 반복문 필요
            InputStream sheetStream = xssfReader.getSheetsData().next();

            InputSource sheetSource = new InputSource(sheetStream);

            //XMLHandler 생성
            ContentHandler handler = new XSSFSheetXMLHandler(styles, data, sheetHandler, false);

            //SAX 형식의 XMLReader 생성
            XMLReader sheetParser = SAXHelper.newXMLReader();

            //XMLReader에 재정의하여 구현한 인터페이스 설정
            sheetParser.setContentHandler(handler);

            //파싱하여 처리
            sheetParser.parse(sheetSource);

            sheetStream.close();

            // Header List : sheetHandler.getHeader();

            // Rows List : sheetHandler.getRows();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        return sheetHandler;
    }

}
