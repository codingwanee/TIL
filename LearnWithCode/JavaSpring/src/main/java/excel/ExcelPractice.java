//package excel;
//
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.util.IOUtils;
//import org.apache.poi.xssf.streaming.SXSSFSheet;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ExcelReal {
//
//    @Value("${jobs.file.device-list.dir:/data/work/dtag/nginx/html/files/device}")
//    private String divcDir;
//
//    @Value("${jobs.file.device-list.max-row:200000}")
//    private int maxRow;
//
//    SaveDeviceFileService saveDeviceFileService = new SaveDeviceFileService();
//
//    // 전달의 마지막날
//    LocalDateTime now = LocalDateTime.now();
//    String todayStr = now.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//    LocalDateTime lastDayOfPreviousMonth =
//            TimeUtil.getLastDayOfPreviousMonth(now.getYear(), now.getMonthValue());
//    String lastDayOfPreviousMonthStr =
//            lastDayOfPreviousMonth.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//    // yyyy
//    String subDir = File.separator + lastDayOfPreviousMonthStr.substring(0, 4);
//
//    // check directory
//    File dir = new File(Paths.get(divcDir + subDir).toString());
//
//    public void justToControll(OperatorDto.LossDeviceBody lossDeviceBody) throws Exception {
//        List<OperatorDto.LossDevice> divcList = lossDeviceBody.getDivcList();
//
//        List<DmtDivcDao> daoList = new ArrayList<>();
//
//        int cnt = 0;
//
//        for (OperatorDto.LossDevice lossDevice : divcList) {
//            String divcNo = lossDevice.getDivcNo();
//            DmtDivcDao dao = new DmtDivcDao();
//            dao.setDivcNo(divcNo);
//
//            daoList.add(dao);
//        }
//
//        Date date = new Date();
//
////        fromJavaSpring();
//        hi("LUXROBO", daoList, date);
//
//    }
//
//    /**
//     * 디바이스 파일 재생성
//     */
//    public void testWriteFile(OperatorDto.LossDeviceBody lossDeviceBody) {
//
//        List<OperatorDto.LossDevice> divcList = lossDeviceBody.getDivcList();
//
//
//        // 파일 복사본 만들기
////    if(!luxDivcList.isEmpty()) {
//        try {
//
//            // 파일 복사 - 기존에 복사된 파일이 존재하면 덮어쓰기
////            Files.copy(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//            // 복사된 파일 읽기
////            FileInputStream fis = new FileInputStream(newFile);
////            XSSFWorkbook workbook = new XSSFWorkbook(fis);
////            File testfile = new File("/Users/wanee/Documents/study/JavaSpring/src/main/resources/TestExcel.xlsx");
//
//            // 로컬 테스트파일
//            // File testfile = new File("/Users/wanee/Documents/study/JavaSpring/src/main/resources/TestExcel.xlsx");
//
//            // 서버 테스트파일
//            File samplefile = new File(Paths.get(divcDir + subDir + File.separator + "sample.xlsx").toString());
//            File testfile = new File(Paths.get(divcDir + subDir + File.separator + "TestFile.xlsx").toString());
//
//            FileInputStream fis = new FileInputStream(samplefile);
//            XSSFWorkbook workbook = new XSSFWorkbook(fis);
//
//            FileInputStream fis2 = new FileInputStream(testfile);
//            XSSFWorkbook workbook2 = new XSSFWorkbook(fis2);
//
//            // sheet 개수 가져오기 - 모든 sheet 조회
//            int sheets = workbook.getNumberOfSheets();
//
//            // 시트별로 반복
//            for (int i = 0; i < 1; i++) {
//                Sheet sheet = workbook.getSheetAt(i);
//                Sheet sheet2 = workbook2.getSheetAt(i);
//
//                // dao별로 반복
//                for (OperatorDto.LossDevice dao : divcList) {
//                    String divcNo = dao.getDivcNo();
//
//                    // TODO 잔존가액 계산해서 넣어주기
//
//                    // 로우를 하나하나 확인
//                    for (int j = 0; j < 5; j++) {
//                        log.info("############## per row ");
//
//                        Cell cell = sheet.getRow(j).getCell(1); // B열만 훑기
//                        String cellVal = cell.toString();
//                        log.info("@@@ ZED1: " + cell.getCellType().name());
//
//                        Cell cell2 = sheet2.getRow(j).getCell(1); // B열만 훑기
//                        log.info("@@@ ZED2: " + cell2.getCellType().name());
//
//                        cell.setCellValue("SAMPLE");
//                        cell2.setCellValue("TEST");
//                        log.info("#### is it changed (sample) : " + cell);
//                        log.info("#### is it change (test): " + cell2);
//
//                        // 특정 값 찾기
//                        if (cellVal.equals(divcNo)) {
//
//                            Cell divcOpStCd = sheet.getRow(j).getCell(0);
//                            Cell divcOpStCd2 = sheet2.getRow(j).getCell(0);
//
////                            srAmt.setCellValue(); // TODO 잔존가액
//                            divcOpStCd.setCellValue("DONE");
//                            divcOpStCd2.setCellValue("DONE");
//
//                            log.info("#### is sample changed : " + divcOpStCd);
//                            log.info("#### is test changed : " + divcOpStCd2);
//
//                        }
//                    }
//                }
//            }
//
//            // TODO 최종 저장
//            FileOutputStream fos = null;
//            FileOutputStream fos2 = null;
//
//            try {
//                fos = new FileOutputStream(samplefile);
//                fos2 = new FileOutputStream(testfile);
//                workbook.write(fos);
//                workbook2.write(fos2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                IOUtils.closeQuietly(workbook);
//                IOUtils.closeQuietly(fos);
//
//                IOUtils.closeQuietly(workbook2);
//                IOUtils.closeQuietly(fos2);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException("fail to renew excel file " + e.getMessage());
//        }
//    }
//
//
//    /**
//     * 대용량 파일처리 확인 ( n십만건, 시트 여러개 )
//     */
//    public void testBigFile(OperatorDto.LossDeviceBody lossDeviceBody) {
//
//        List<OperatorDto.LossDevice> divcList = lossDeviceBody.getDivcList();
//
//        // 전달의 마지막날
//        LocalDateTime now = LocalDateTime.now();
//        String todayStr = now.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//        LocalDateTime lastDayOfPreviousMonth =
//                TimeUtil.getLastDayOfPreviousMonth(now.getYear(), now.getMonthValue());
//        String lastDayOfPreviousMonthStr =
//                lastDayOfPreviousMonth.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//        // yyyy
//        String subDir = File.separator + lastDayOfPreviousMonthStr.substring(0, 4);
//
//        // 파일 & 디렉토리 존재여부 확인
////        if (!dir.exists() && !dir.mkdirs()) {
////            log.error("File or Directory does not exist, {}", dir.getAbsolutePath());
////            throw new ServiceException(RestfulErrorCode.INTERNAL_SERVER_ERROR);
////        }
//
//        // original file name
//        StringBuffer sb = new StringBuffer();
//        sb.append(EXCEL_RELEASE_DEVICE_PREFIX);
//        sb.append("UNICK"); // FIXME 테스트용 하드스트링
//        sb.append(" until ");
//        sb.append(lastDayOfPreviousMonthStr);
//        sb.append("." + ExcelConstants.XLSX);
//
//        // 파일 복사본 만들기
////    if(!luxDivcList.isEmpty()) {
//        try {
//            // 복사할 오리지널 파일
//            File orgFile =
//                    new File(Paths.get(divcDir + subDir + File.separator + sb.toString()).toString());
//
//            // 복사된 파일
//            // (yyyy-MM-dd)Release Device of UNICK until yyyy-MM-dd.xlsx
//            File newFile =
//                    new File(
//                            Paths.get(divcDir + subDir + File.separator + "(" + todayStr + ")" + sb.toString())
//                                    .toString());
//
//            log.info("#### orgFile : " + orgFile.getName());
//            log.info("#### newFile : " + newFile.getName());
//
//            // 파일 복사 - 기존에 복사된 파일이 존재하면 덮어쓰기
//            Files.copy(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//            // 복사된 파일 읽기
//            FileInputStream fis = new FileInputStream(newFile);
//            XSSFWorkbook workbook = new XSSFWorkbook(fis);
//
//
//            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook);
//
//            // sheet 개수 가져오기 - 모든 sheet 조회
//            int sheets = sxssfWorkbook.getNumberOfSheets();
//
//            // 시트별로 반복
//            for (int i = 0; i < sheets; i++) {
//                Sheet sheet = sxssfWorkbook.getSheetAt(i);
//
//                // dao별로 반복
//                for (OperatorDto.LossDevice dao : divcList) {
//                    // 로우별 반복
//                    for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
//
//
//                        Cell cell = sheet.getRow(j).getCell(1); // B열만 훑기
//                        cell.setCellValue("checked");
//                        log.info("#### after checked : " + cell);
//                    }
//                }
//            }
//
//            // TODO 최종 저장
//            FileOutputStream fos = null;
//
//            try {
//                log.info("#### file save ! file name : " + newFile.getName());
//                fos = new FileOutputStream(newFile);
//                sxssfWorkbook.write(fos);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                IOUtils.closeQuietly(sxssfWorkbook);
//                IOUtils.closeQuietly(workbook);
//                IOUtils.closeQuietly(fos);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException("fail to renew excel file " + e.getMessage());
//        }
////
//    }
//
//
//    public void startFromTheBottom() {
//
//        // 전달의 마지막날
//        LocalDateTime now = LocalDateTime.now();
//        String todayStr = now.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//        LocalDateTime lastDayOfPreviousMonth =
//                TimeUtil.getLastDayOfPreviousMonth(now.getYear(), now.getMonthValue());
//        String lastDayOfPreviousMonthStr =
//                lastDayOfPreviousMonth.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DAY));
//
//        // yyyy
//        String subDir = File.separator + lastDayOfPreviousMonthStr.substring(0, 4);
//
//        // check directory
////        String babyfile = Paths.get(divcDir + subDir + File.separator + "babyfile.xlsx").toString();
////        File babyfile = new File(Paths.get(divcDir + subDir + File.separator + "babyfile.xlsx").toString());
//        String babyfile = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/babyfile.xlsx";
//
//
//        //.xlsx 확장자 지원
//        XSSFWorkbook xssfWb = null; // .xlsx
//        XSSFSheet xssfSheet = null; // .xlsx
//        XSSFRow xssfRow = null; // .xlsx
//        XSSFCell xssfCell = null;// .xlsx
//
//        try {
//            int rowNo = -1; // 행 갯수
//            int colNo = -1;
//
//            // 워크북 생성
//            xssfWb = new XSSFWorkbook();
//            xssfSheet = xssfWb.createSheet("엑셀 테스트"); // 워크시트 이름
//
//
//            //헤더 생성
//            xssfRow = xssfSheet.createRow(++rowNo); //헤더 01
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//
//            xssfRow = xssfSheet.createRow(rowNo++);
//            colNo = -1;
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfRow = xssfSheet.createRow(rowNo++);
//            colNo = -1;
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfRow = xssfSheet.createRow(rowNo++);
//            colNo = -1;
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfRow = xssfSheet.createRow(rowNo++);
//            colNo = -1;
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//            xssfCell = xssfRow.createCell(++colNo);
//            xssfCell.setCellValue("aaaaa");
//
//            File file = new File(babyfile);
//            FileOutputStream fos = null;
//            fos = new FileOutputStream(file);
//            xssfWb.write(fos);
//
//            if (xssfWb != null) xssfWb.close();
//            if (fos != null) fos.close();
//
//        } catch (Exception e) {
//
//        } finally {
//
//        }
//
//    }
//
//    // Save excel file of device to server
//    @edu.umd.cs.findbugs.annotations.SuppressWarnings("PATH_TRAVERSAL_IN")
//    public void saveExcelFile(List<DmtDivcDao> list, String deviceGroup, Date rdtDpOpndt) throws IOException, InvalidFormatException {
//
//        List<DeviceDto.ReadDeviceExcel> deviceList =
//                list.stream()
//                        .map(
//                                dao ->
//                                        new DeviceDto.ReadDeviceExcel(
//                                                dao.getRecid(),
//                                                dao.getDivcNo(),
//                                                dao.getPlyno(),
//                                                dao.getInsClstr(),
//                                                dao.getEncfgInfo(),
//                                                dao.getDivcNm(),
//                                                dao.getEsimNo(),
//                                                dao.getEsimTlno(),
//                                                dao.getCmBzpsRgtDscno(),
//                                                dao.getMnftrNm(),
//                                                dao.getMdnm(),
//                                                dao.getDivcSrlNo(),
//                                                dao.getCqVlamt(),
//                                                // 잔존가액 입력
//                                                dao.getCqVlamt() == null
//                                                        ? null
//                                                        : SurvivalAmountUtil.getCurrentDateSurvivalAmount(
//                                                        dao.getCqVlamt(), dao.getRdtDpOpndt()),
//                                                dao.getPrddt(),
//                                                dao.getNtgdt(),
//                                                dao.getMfgdt(),
//                                                dao.getOpndt(),
//                                                dao.getRdtDpOpndt(),
//                                                DivcOpStcd.findByCode(dao.getDivcOpStcd()) == null
//                                                        ? dao.getDivcOpStcd()
//                                                        : DivcOpStcd.findByCode(dao.getDivcOpStcd()).getDesc(),
//                                                StockStYn.findByCode(dao.getStockStYn()) == null
//                                                        ? dao.getStockStYn()
//                                                        : StockStYn.findByCode(dao.getStockStYn()).getDesc(),
//                                                dao.getUsStrdt(),
//                                                dao.getUsNddt(),
//                                                DivcStcd.findByCode(dao.getDivcStcd()) == null
//                                                        ? dao.getDivcStcd()
//                                                        : DivcStcd.findByCode(dao.getDivcStcd()).getDesc(),
//                                                dao.getFotaVerVl(),
//                                                dao.getFotaMdfDthms()))
//                        .collect(Collectors.toList());
//
//        // date
//        Instant now = Instant.now();
//        SimpleDateFormat dayFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DAY);
//        SimpleDateFormat yearFormat = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR);
//        String todayStr = dayFormat.format(Date.from(now));
//        String rdtDpOpndtStr = dayFormat.format(rdtDpOpndt);
//        String rdtDpOpndtYearStr = yearFormat.format(rdtDpOpndt);
//
//        // yyyy
//        String subDir = File.separator + rdtDpOpndtYearStr;
//
//        // check directory
//        File dir = new File(Paths.get(divcDir, subDir).toString());
//        if (!dir.exists() && !dir.mkdirs()) {
//            log.error("Failed to create directory, {}", dir.getAbsolutePath());
//            throw new ServiceException(RestfulErrorCode.INTERNAL_SERVER_ERROR);
//        }
//
//        // Generate file name
////        StringBuffer sb = new StringBuffer();
////        sb.append(EXCEL_RELEASE_DEVICE_PREFIX);
////        sb.append(deviceGroup);
////        sb.append(" until ");
////        sb.append(rdtDpOpndtStr);
////        sb.append("." + ExcelConstants.XLSX);
//
//        // (yyyy-MM-dd)Release Device of UNICK until yyyy-MM-dd.xlsx
//        File file =
//                new File(
//                        Paths.get(divcDir + subDir + File.separator + "(" + todayStr + ")" + "HAHAHA")
//                                .toString());
//
//        // 시트 당 최대 maxRow 개가 되도록 구성
////        SXSSFWorkbook sxssfWorkbook = createSXSSFWorkbook(Deflater.BEST_COMPRESSION, 500, true, false);
////        sxssfWorkbook.setCompressTempFiles(true);
//
//        XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//        List<List<DeviceDto.ReadDeviceExcel>> subLists = chopped(deviceList, maxRow);
//        for (int i = 0; i < subLists.size(); i++) {
//            List<DeviceDto.ReadDeviceExcel> subList = subLists.get(i);
//            // sheet: 1. 100000, 2. 100000, 3. 2578
//            XSSFSheet sheet = workbook.createSheet((i + 1) + ". " + subList.size());
////            writeHeaderRow(DeviceDto.ReadDeviceExcel.getTitleList(), sheet);
////            SXSSFSheet sheet = sxssfWorkbook.createSheet((i + 1) + ". " + subList.size());
////            writeHeaderRow(DeviceDto.ReadDeviceExcel.getTitleList(), sheet);
////            writeDataRow(subList, sxssfWorkbook, sheet);
//        }
//
//        log.debug("{} size={}", file.getName(), deviceList.size());
//
//        // Save File
//        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//            workbook.write(fileOutputStream);
//            workbook.close();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//
//    public void testBigFile() throws Exception {
//
//        String orgfile = divcDir + subDir + File.separator + "orgfile.xlsx";
//        String newfile = divcDir + subDir + File.separator + "newfile.xlsx";
//
//        File file = new File(orgfile);
//
//        // 기존 파일
//        ExcelParsingUtil excelSheetHandler = ExcelParsingUtil.readExcel(file);
//        List<List<String>> datas = excelSheetHandler.getRows();
//
//        // 새로 만들 파일
//        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
//        Sheet sxSheet = sxssfWorkbook.createSheet("sheet");
////        int sheetNum = workbook.getNumberOfSheets();
//
//        int rowCnt = -1;
//        int cellCnt = -1;
//
//        // 새 파일에 데이터 복사
//        for (List<String> row : datas) {
//            rowCnt++;
//            cellCnt = -1;
//
//            Row sxSheetRow = sxSheet.createRow(rowCnt);
//
//            for (String cell : row) { // row 하나를 읽어온다.
//                cellCnt++;
////                System.out.println(cell); // cell 하나를 읽어온다.
//
//                Cell sxCell = sxSheetRow.createCell(cellCnt);
//
//                if (cell.equals("find me")) {
//                    sxCell.setCellValue("BOOYA");
//                } else {
//                    sxCell.setCellValue(cell);
//                }
//            }
//        }
//
////        for (int k = 0; k < sheetNum; k++) {
////
////            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
////                Row row = sheet.getRow(i);
////                Row shrow = sxSheet.createRow(i);
////
////                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
////                    Cell cell = row.getCell(j);
////                    Cell sxcell = shrow.createCell(j);
////
////                    if (cell.toString().equals("aaa")) {
////                        sxcell.setCellValue("123");
////                    } else {
////                        sxcell.setCellValue(cell.toString());
////                    }
////                }
////            }
////        }
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(newfile);
//            sxssfWorkbook.write(fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            fos.close();
////            workbook.close();
//            sxssfWorkbook.close();
//            sxssfWorkbook.dispose();
//        }
//
//
//    }
//
//
//    public void reviseDeviceFile(String deviceGroup, List<DmtDivcDao> daoList, Date date) throws Exception {
//
//
//        String newFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/fromJavaSpring.xlsx";
//        String orgFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/babyfile.xlsx";
//        File newFile = new File(newFilePath);
//        File orgFile = new File(orgFilePath);
//
//        // 변경할 열 index
//        final int cellPlyno = 2;      // 증권종기
//        final int cellInsClstr = 3;   // 보험종기
//        final int cellSrAmt = 13;      // 잔존가액
//        final int cellDivcOpStCd = 19; // 디바이스운용상태코드
//        final int cellUsNddt = 22;    // 사용종료일자
//        final int cellDivcStCd = 23;  // 디바이스 상태코드
//
//        boolean flag;
//
//        if (!daoList.isEmpty()) {
//
//            // 기존 파일
//            ExcelParsingUtil excelSheetHandler = ExcelParsingUtil.readExcel(orgFile);
//            List<List<String>> datas = excelSheetHandler.getRows();
//
//            // 새로 만들 파일
//            SXSSFWorkbook workbook = createSXSSFWorkbook(9, 500, true, false);
//
//            int totalRowNum = datas.size();
//
//            log.info("processing data " + totalRowNum + " rows");
//
//            int rowCnt = 0;
//            int cellCnt = -1;
//
//            // 시트당 20000개씩
//            List<List<List<String>>> choppedDatas = ExcelUtil.chopped(datas, 199999);
//
//            // 새 파일에 데이터 입력
//            for (int k = 0; k < excelSheetHandler.getSheetNum(); k++) { // 시트별 프로세스
//
//                log.info("excelSheetHandler.getSheetNum()" + excelSheetHandler.getSheetNum());
//
//                log.info("### index of k : " + k);
//
//                // 시트 생성
//                SXSSFSheet sheet = workbook.createSheet("sheet" + k);
//
//                log.info("설마.." + k);
//
//                // 헤더 입력
//                writeHeaderRow(DeviceDto.ReadDeviceExcel.getTitleList(), sheet);
//
//                rowCnt = 0;
//
//                // 새 파일에 데이터 복사
//                for (List<String> row : choppedDatas.get(k)) { // row를 하나씩 읽어온다
//                    Row sxSheetRow = sheet.createRow(++rowCnt);
//
//                    log.info("### loop with k : " + k);
//
//                    for (DmtDivcDao dao : daoList) {
//                        String divcNo = dao.getDivcNo();
//                        cellCnt = -1;
//                        flag = false;
//
//                        for (String cell : row) { // cell을 하나씩 읽어온다
//                            Cell sxCell = sxSheetRow.createCell(++cellCnt);
//
//                            if (cellCnt == 1) { // B열 검사
//                                if (cell.equals(divcNo)) {
//                                    flag = true;
//                                }
//                            }
//
//                            if (flag) {
//                                long srAmtcal = 9999;
//
//                                switch (cellCnt) {
//                                    case cellPlyno:
//                                        sxCell.setCellValue("");
//                                        break;
//                                    case cellInsClstr:
//                                        sxCell.setCellValue("");
//                                        break;
//                                    case cellDivcOpStCd:
//                                        sxCell.setCellValue("미사용");
//                                        break;
//                                    case cellSrAmt:
//                                        sxCell.setCellValue(srAmtcal);
//                                        break;
//                                    case cellUsNddt:
//                                        sxCell.setCellValue(dao.getUsNddt());
//                                        break;
//                                    case cellDivcStCd:
//                                        sxCell.setCellValue("분실");
//                                        break;
//                                    default:
//                                        sxCell.setCellValue(cell);
//                                        break;
//                                }
//                            } else {
//                                sxCell.setCellValue(cell);
//                            }
//                        }
//                    }
//
//                    log.info("### kkkk : " + k);
//                }
//            }
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(newFile);
//                workbook.write(fos);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                fos.close();
//                workbook.close();
//                workbook.dispose();
//            }
//        } else {
//            log.info ("device of " + deviceGroup + " is not exist in list");
//        }
//    }
//
//
//
//    public void hi(String deviceGroup, List<DmtDivcDao> daoList, Date date) throws Exception {
//
//        String orgFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/babyfile.xlsx";
//        String newFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/newfile.xlsx";
//
//        File orgFile = new File(orgFilePath);
//        File newFile = new File(newFilePath);
//
//
//        // 변경할 열 index
//        final int cellPlyno = 2;      // 증권종기
//        final int cellInsClstr = 3;   // 보험종기
//        final int cellSrAmt = 13;      // 잔존가액
//        final int cellDivcOpStCd = 19; // 디바이스운용상태코드
//        final int cellUsNddt = 22;    // 사용종료일자
//        final int cellDivcStCd = 23;  // 디바이스 상태코드
//
//        boolean flag;
//
//        if (!daoList.isEmpty()) {
//
//            // 기존 파일
//            ExcelParsingUtil excelSheetHandler = ExcelParsingUtil.readExcel(orgFile);
//            List<List<String>> datas = excelSheetHandler.getRows();
//
//
//            // 새로 만들 파일
//            SXSSFWorkbook workbook = createSXSSFWorkbook(9, 500, true, false);
//
//            int totalRowNum = datas.size();
//
//            log.info("processing data " + totalRowNum + " rows");
//
//            int rowCnt = 0;
//            int cellCnt = -1;
//
//            // 시트당 20000개씩
//            List<List<List<String>>> choppedDatas = ExcelUtil.chopped(datas, 6);
//
//            List<DeviceDto.ReadDeviceExcel> deviceList = new ArrayList<>();
//
//            // 새 파일에 데이터 입력
//            for (int k = 0; k < excelSheetHandler.getSheetNum(); k++) { // 시트별 프로세스
//
//                log.info("### excelSheetHandler.getSheetNum() " + excelSheetHandler.getSheetNum());
//
//                // 시트 생성
//                SXSSFSheet sheet = workbook.createSheet("sheet" + (k + 1));
//
//                // 헤더 입력
//                writeHeaderRow(DeviceDto.ReadDeviceExcel.getTitleList(), sheet);
//
//                rowCnt = 0;
//
//                // 새 파일에 데이터 복사
//                for (List<String> row : choppedDatas.get(k)) { // row를 하나씩 읽어온다
//                    Row sxSheetRow = sheet.createRow(++rowCnt);
//
//                    for (DmtDivcDao dao : daoList) {
//                        String divcNo = dao.getDivcNo();
//                        cellCnt = -1;
//                        flag = false;
//
//                        for (String cell : row) { // cell을 하나씩 읽어온다
//                            Cell sxCell = sxSheetRow.createCell(++cellCnt);
//
//                            if (cellCnt == 1) { // B열 검사
//                                if (cell.equals(divcNo)) {
//                                    flag = true;
//                                }
//                            }
//
//                            if (flag) {
//                                long srAmtcal = 9999;
//
//                                switch (cellCnt) {
//                                    case cellPlyno:
//                                        sxCell.setCellValue("");
//                                        break;
//                                    case cellInsClstr:
//                                        sxCell.setCellValue("");
//                                        break;
//                                    case cellDivcOpStCd:
//                                        sxCell.setCellValue("미사용");
//                                        break;
//                                    case cellSrAmt:
//                                        sxCell.setCellValue(srAmtcal);
//                                        break;
//                                    case cellUsNddt:
//                                        sxCell.setCellValue(dao.getUsNddt());
//                                        break;
//                                    case cellDivcStCd:
//                                        sxCell.setCellValue("분실");
//                                        break;
//                                    default:
//                                        sxCell.setCellValue(cell);
//                                        break;
//                                }
//                            } else {
//                                sxCell.setCellValue(cell);
//                            }
//                        }
//                    }
//                }
//            }
//
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(newFile);
//                workbook.write(fos);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                fos.close();
//                workbook.close();
//                workbook.dispose();
//            }
//        } else {
//            log.error("Device List is not exist.");
//            throw new ServiceException(RestfulErrorCode.NOT_EXIST);
//        }
//    }
//
//
//    public void fromJavaSpring() throws Exception {
//
//        String newFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/fromJavaSpring.xlsx";
//        String orgFilePath = "/Users/wanee/Documents/study/JavaSpring/src/main/resources/megafile.xlsx";
//        File newfile = new File(newFilePath);
//        File file = new File(orgFilePath);
//
//        // 기존 파일
//        ExcelParsingUtil excelSheetHandler = ExcelParsingUtil.readExcel(file);
//        List<List<String>> datas = excelSheetHandler.getRows();
//
//        // 새로 만들 파일
//        SXSSFWorkbook sxssfWorkbook = createSXSSFWorkbook(9, 500, true, false);
//
//        int totalRowNum = datas.size();
//        System.out.println("size : " + totalRowNum);
//
//        int rowCnt = 0;
//        int cellCnt = -1;
//
//        List<List<List<String>>> choppedDatas = ExcelUtil.chopped(datas, 199999);
//
//        // 새 파일에 데이터 복사
//        for (int k = 0; k < excelSheetHandler.getSheetNum(); k++) { // 시트별 작업
//
//            SXSSFSheet sxSheet = sxssfWorkbook.createSheet("sheet" + k);
//            Row headerRow = sxSheet.createRow(0);
//
//            // 시트별 헤더 입력
//            for (int i = 0; i < excelSheetHandler.getMaxRow(); i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(excelSheetHandler.getHeader().get(i));
//            }
//
//            rowCnt = 0;
//
//            for (List<String> row : choppedDatas.get(k)) { // row별 작업
////                rowCnt++;
//                cellCnt = -1;
//
//                Row sxSheetRow = sxSheet.createRow(++rowCnt);
//
//                for (String cell : row) { // cell을 하나씩 읽어온다
//                    cellCnt++;
//                    Cell sxCell = sxSheetRow.createCell(cellCnt);
//
//                    if (cell.equals("find me")) {
//                        sxCell.setCellValue("BOOYA");
//                    } else {
//                        sxCell.setCellValue(cell);
//                    }
//
//                }
//            }
//        }
//
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(newfile);
//            sxssfWorkbook.write(fos);
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        } finally {
//            fos.close();
//            sxssfWorkbook.close();
//            sxssfWorkbook.dispose();
//        }
//
//    }
//
//}
