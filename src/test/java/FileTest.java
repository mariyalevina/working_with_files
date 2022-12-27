import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
        String source = "./src/test/resources/Zip.zip";
        String destination = "./src/test/resources/zip/";
        String password = "zxc";

        ZipFile zipFile = new ZipFile(source);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(destination);
        String result;
        try (FileInputStream stream = new FileInputStream("./src/test/resources/zip/TXT.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("проверка файла в архиве");
        FileUtils.deleteDirectory(new File("./src/test/resources/zip/"));
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
