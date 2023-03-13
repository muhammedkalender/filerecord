package dev.siyah.filemanager.utility;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FileUtilityTest {
    private FileUtility fileUtility;

    @BeforeTest
    public void setUp(){
        this.fileUtility = new FileUtility();
    }

    @Test
    public void testGetExtensionByFileName() {
       String actual = this.fileUtility.getExtensionByFileName("test.png");
       String excepted = "png";

        Assert.assertEquals(actual, excepted);
    }

    @Test
    public void testGetExtensionByFileName_WhenExtensionNull() {
        String actual = this.fileUtility.getExtensionByFileName("test");
        String excepted = "";

        Assert.assertEquals(actual, excepted);
    }
}