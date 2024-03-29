package be.brambasiel.unpacker;

import org.junit.jupiter.api.*;

import java.io.File;
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

    private File getTestFile(String fileName) {
        URL res = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (res == null) {
            throw new RuntimeException("Test file not found: " + fileName);
        }
        File file = new File(res.getFile());
        assertTrue(file.exists());
        return file;
    }

    private Path getTempPath() {
        File tempFolder = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(tempFolder.exists());
        return tempFolder.toPath();
    }

    private File createTestFolder(String name) {
        String fullName = name + "_" + System.currentTimeMillis();
        File tempFolder = getTempPath().resolve(fullName).toFile();
        assertTrue(tempFolder.mkdir());
        tempFolders.add(tempFolder);
        logger.fine(() -> "Created temp folder: " + tempFolder.getAbsolutePath());
        return tempFolder;
    }

    @BeforeAll
    public static void init() {
        logger = Logger.getLogger(ImageUnpackerTest.class.getName());
        logger.setLevel(Level.ALL);
        tempFolders = new HashSet<>();
    }

    private static void deleteFolder(File folder) {
        // Delete all files in the folder
        File[] files = folder.listFiles();
        assertNotNull(files);
        for (File file : files) {
            assertTrue(file.delete());
            logger.fine(() -> "Deleted temp file: " + file.getAbsolutePath());
        }

        assertTrue(folder.delete());
        logger.fine(() -> "Deleted temp folder: " + folder.getAbsolutePath());
    }

    @AfterAll
    public static void cleanup() {
        tempFolders.stream().filter(File::exists).forEach(ImageUnpackerTest::deleteFolder);
    }

    @Test
    void checkTestEnvironment() {
        File tempFolder = createTestFolder("test");
        assertTrue(tempFolder.exists());
        assertTrue(tempFolder.isDirectory());
    }

    // Test a small image
    @Test
    void unpackShapeImage() {
        File imageFile = getTestFile("shapes.png");
        File tempFolder = createTestFolder("shapes_out");

        ImageUnpacker.run(imageFile, tempFolder);

        File[] files = tempFolder.listFiles();
        assertNotNull(files);
        assertEquals(4, files.length);
    }

//    Test a bigger image
//    @Test
//    void unpackAsteroidsImage() {
//        File imageFile = getTestFile("asteroids.png");
//        File tempFolder = createTestFolder("asteroids_out");
//
//        ImageUnpacker.run(imageFile, tempFolder);
//
//        File[] files = tempFolder.listFiles();
//        assertNotNull(files);
//        assertEquals(4, files.length);
//    }
}
