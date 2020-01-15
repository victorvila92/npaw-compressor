import com.compressor.Compressor;
import com.compressor.exceptions.CommandLineArgumentException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompressorTest {

    private static final String ORIGINAL_FILENAME = "integers";

    @Test(expected = CommandLineArgumentException.class)
    public void wrongArgumentOperation() throws Exception {
        String[] args = {"-p", "filename1", "filename2"};
        Compressor.main(args);
    }
    @Test(expected = CommandLineArgumentException.class)
    public void wrongArgumentEntries() throws Exception {
        String[] args = {"-c", "filename1", "filename2", "random"};
        Compressor.main(args);
    }

    @Test
    public void compressionWorking() throws Exception {
        String initialFileName = ORIGINAL_FILENAME;
        String compressedFileName = "compressed";

        String[] args = {"-c", initialFileName, compressedFileName};
        Compressor.main(args);
        File initialFile = new File(initialFileName);
        File compressedFile = new File(compressedFileName);

        assertTrue(initialFile.exists());
        assertTrue(compressedFile.exists());
        assertTrue(initialFile.length() > compressedFile.length());
    }

    @Test
    public void decompressionWorking() throws Exception {
        String initialFileName = "compressed";
        String decompressedFileName = "decompressed";

        String[] args = {"-d", initialFileName, decompressedFileName};
        Compressor.main(args);
        File initialFile = new File(initialFileName);
        File decompressedFile = new File(decompressedFileName);
        File originalFile = new File(ORIGINAL_FILENAME);

        assertTrue(initialFile.exists());
        assertTrue(decompressedFile.exists());
        assertTrue(originalFile.exists());
        assertTrue(initialFile.length() < decompressedFile.length());
        assertEquals(originalFile.length(), decompressedFile.length());
        assertTrue(decompressedFile.delete());
    }
}
