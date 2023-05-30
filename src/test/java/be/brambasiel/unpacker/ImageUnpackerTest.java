package be.brambasiel.unpacker;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ImageUnpackerTest {
    private static Logger logger;
    private static Set<File> tempFolders;

    private File getTestFile(String fileName){
        URL res = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (res == null){
            throw new RuntimeException("Test file not found: " + fileName);
        }
        File file = new File(res.getFile());
        assertTrue(file.exists());
        return file;
    }

    private Path getTempPath(){
        File tempFolder = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(tempFolder.exists());
        return tempFolder.toPath();
    }

    private File createTestFolder(String name){
        String fullName = name + "_" + System.currentTimeMillis();
        File tempFolder = getTempPath().resolve(fullName).toFile();
        assertTrue(tempFolder.mkdir());
        tempFolders.add(tempFolder);
        logger.fine(() -> "Created temp folder: " + tempFolder.getAbsolutePath());
        return tempFolder;
    }

    @BeforeAll
    public static void init(){
        logger = Logger.getLogger(ImageUnpackerTest.class.getName());
        logger.setLevel(Level.ALL);
        tempFolders = new HashSet<>();
    }

    private static void deleteFolder(File folder){
        // Delete all files in the folder
        File[] files = folder.listFiles();
        assertNotNull(files);
        for (File file : files){
            assertTrue(file.delete());
            logger.fine(() -> "Deleted temp file: " + file.getAbsolutePath());
        }

        assertTrue(folder.delete());
        logger.fine(() -> "Deleted temp folder: " + folder.getAbsolutePath());
    }

    @AfterAll
    public static void cleanup(){
        tempFolders.stream().filter(File::exists).forEach(ImageUnpackerTest::deleteFolder);
    }

    @Test
    public void checkTestEnvironment(){
        File tempFolder = createTestFolder("test");
        assertTrue(tempFolder.exists());
        assertTrue(tempFolder.isDirectory());
    }

    @Test
    public void unpackShapeImage() {
        File imageFile = getTestFile("shapes.png");
        File tempFolder = createTestFolder("shapes_out");

        try {
            ImageUnpacker unpacker = new ImageUnpacker(imageFile, tempFolder);
            unpacker.unpack();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] files = tempFolder.listFiles();
        assertNotNull(files);
        assertEquals(4,files.length);
    }
}
