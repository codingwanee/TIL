package excel;


/*If memory footprint is an issue, then for XSSF, you can get at the underlying XML data, and process it yourself. This is intended for intermediate developers who are willing to learn a little bit of low level structure of .xlsx files, and who are happy processing XML in java. Its relatively simple to use, but requires a basic understanding of the file structure. The advantage provided is that you can read a XLSX file with a relatively small memory footprint.
One important thing to note with the basic Event API is that it triggers events only for things actually stored within the file. With the XLSX file format, it is quite common for things that have yet to be edited to simply not exist in the file. This means there may well be apparent "gaps" in the record stream, which you need to work around.
To use this API you construct an instance of org.apache.poi.xssf.eventmodel.XSSFReader. This will optionally provide a nice interace on the shared strings table, and the styles. It provides methods to get the raw xml data from the rest of the file, which you will then pass to SAX.
This example shows how to get at a single known sheet, or at all sheets in the file. It is based on the example in svn src/examples/src/org/apache/poi/xssf/eventusermodel/exmaples/FromHowTo.java
*/
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
//import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XSSF and SAX (Event API) basic example.
 *  XSLX processing with the XSSF Event code.
 */
public class SaxExample {



    public void processFirstSheet(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);

            // process the first sheet
            try (InputStream sheet = r.getSheetsData().next()) {
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
            }
        }
    }

    public void processAllSheets(String filename) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ)) {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);

            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                try (InputStream sheet = sheets.next()) {
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                }
                System.out.println("");
            }
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = SAXHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean inlineStr;
        private final LruCache<Integer,String> lruCache = new LruCache<>(50);



        private static class LruCache<A,B> extends LinkedHashMap<A, B> {
            private final int maxEntries;

            public LruCache(final int maxEntries) {
                super(maxEntries + 1, 1.0f, true);
                this.maxEntries = maxEntries;
            }

            @Override
            protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
                return super.size() > maxEntries;
            }
        }

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference


                System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                inlineStr = cellType != null && cellType.equals("inlineStr");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v") || (inlineStr && name.equals("c"))) {
                System.out.println(lastContents);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);

        }
    }


}