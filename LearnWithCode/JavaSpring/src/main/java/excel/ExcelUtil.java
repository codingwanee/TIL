package excel;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;

public class ExcelUtil {
    public static SXSSFWorkbook createSXSSFWorkbook(
            int compressionLevel,
            int rowAccessWindowSize,
            boolean compressTmpFiles,
            boolean useSharedStringsTable) {

        SXSSFWorkbook workbook = null;
        if (compressionLevel != Deflater.DEFAULT_COMPRESSION) {
            workbook =
                    new SXSSFWorkbook(null, rowAccessWindowSize, compressTmpFiles, useSharedStringsTable) {
                        protected ZipArchiveOutputStream createArchiveOutputStream(OutputStream out) {
                            ZipArchiveOutputStream zos = new ZipArchiveOutputStream(out);
                            zos.setUseZip64(Zip64Mode.AsNeeded);
                            zos.setLevel(compressionLevel);
                            return zos;
                        }
                    };
        } else {
            workbook =
                    new SXSSFWorkbook(null, rowAccessWindowSize, compressTmpFiles, useSharedStringsTable);
        }
        return workbook;
    }

    // chops a list into non-view sublists of length L
    static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}