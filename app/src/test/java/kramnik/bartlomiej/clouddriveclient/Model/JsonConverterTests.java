package kramnik.bartlomiej.clouddriveclient.Model;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;

/**
 * Tests of jsonConverter
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Log.class, JSONArray.class })
public class JsonConverterTests {

    private JsonConverter testObject;
    private String correctString;

    @Before
    public void prepare(){
        PowerMockito.mockStatic(Log.class);
        testObject = new JsonConverter(new Gson());
        PowerMockito.mockStatic(JSONArray.class);
        correctString = "[\n" +
                "    {\n" +
                "        \"fileType\": \"Other\",\n" +
                "        \"name\": \"aaa\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%aaa\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Folder\",\n" +
                "        \"name\": \"aaa.png\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%aaa.png\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Other\",\n" +
                "        \"name\": \"add.zip\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%add.zip\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"TextFile\",\n" +
                "        \"name\": \"cv.pdf\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%cv.pdf\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Image\",\n" +
                "        \"name\": \"dodane1.jpg\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%dodane1.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Folder\",\n" +
                "        \"name\": \"folder\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%folder\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Folder\",\n" +
                "        \"name\": \"folder2asd\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%folder2asd\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Program\",\n" +
                "        \"name\": \"gowno.exe\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%gowno.exe\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"fileType\": \"Image\",\n" +
                "        \"name\": \"photo.jpg\",\n" +
                "        \"path\": \"A:%Desktop%CloudDriveServer%.%photo.jpg\"\n" +
                "    }\n" +
                "]";
    }

    @Test(expected = NullPointerException.class)
    public void nullTest(){
        testObject.getFilesList(null);
    }

    @Test
    public void conversionTest1(){
        List<FileDetails> testList = testObject.getFilesList(correctString);
        Assert.assertEquals(9, testList.size());
    }

    @Test
    public void conversionWrongtest(){
        List<FileDetails> testList = testObject.getFilesList("adfaiydtvgfaiyusdgaiysudgyujasdgbjahsgbdjkhasd");
        Assert.assertEquals(0, testList.size());
    }

    @Test
    public void emptyStringConversion(){
        List<FileDetails> testList = testObject.getFilesList("");
        Assert.assertEquals(0, testList.size());
    }

}
