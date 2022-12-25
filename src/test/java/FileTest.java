import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class FileTest {

    @Test
    void fileTxtTest() throws Exception {
        String result;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("Test.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("Проверка текстового файла");
    }

    @Test
    void filePdfTest() throws Exception {
        PDF pdfFile = new PDF(getClass().getClassLoader().getResourceAsStream("PDF.pdf"));
        assertThat(pdfFile.text).contains("Как справляться с тревогой в кризисное время");
    }

    @Test
    void fileXlsTest() throws Exception {
        XLS xlsFile = new XLS(getClass().getClassLoader().getResourceAsStream("Xlsx.xlsx"));
        assertThat(xlsFile.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue()).isEqualTo("Сергей");
    }

    @Test
    void fileZipTest() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String entryAsString = null;
        try (ZipInputStream stream = new ZipInputStream(classLoader.getResourceAsStream("Txt.zip"))) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                entryAsString = IOUtils.toString(stream, StandardCharsets.UTF_8);
            }
        }
        Assertions.assertTrue(entryAsString.contains("проверка файла в архиве"));
    }

    @Test
    void fileDocxTest() throws Exception {
        String text;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("Docx.docx")) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(stream);
            text = wordMLPackage.getMainDocumentPart().getContent().toString();
        }
        assertThat(text).contains("Тест2");
    }
}
