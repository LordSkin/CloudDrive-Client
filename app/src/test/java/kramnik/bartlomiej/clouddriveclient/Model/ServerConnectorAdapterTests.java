package kramnik.bartlomiej.clouddriveclient.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorAdapter;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

/**
 * Tests of ServerConnectorAdapter
 */

public class ServerConnectorAdapterTests {

    ServerConnectorAdapter testObject;

    @Before
    public void prepare() {
        ServerConnector serverConnector = new ServerConnector() {
            @Override
            public void getFile(String url, String name, ProgressIndicator indicator) throws IOException {
                if (url == null || name == null || indicator == null)
                    throw new NullPointerException();
            }

            @Override
            public File getFile(String url, String name) throws IOException {
                if (url == null || name == null) throw new NullPointerException();
                return null;
            }

            @Override
            public List<FileDetails> getList(String url, JsonConverter converter) throws IOException {
                if (url == null || converter == null) throw new NullPointerException();
                return null;
            }

            @Override
            public boolean delete(String url) throws IOException {
                if (url == null) throw new NullPointerException();
                return false;
            }

            @Override
            public boolean rename(String url, String newName) {
                if (url == null || newName == null) throw new NullPointerException();
                return false;
            }

            @Override
            public boolean addFile(File file, String url) throws IOException {
                if (file == null | url == null) throw new NullPointerException();
                return false;
            }

            @Override
            public boolean addFolder(String url) throws IOException {
                if (url == null) throw new NullPointerException();
                return false;
            }

            @Override
            public boolean ping() {
                return false;
            }
        };
        testObject = new ServerConnectorAdapter(serverConnector, Mockito.mock(JsonConverter.class));
    }

    @Test(expected = NullPointerException.class)
    public void nulltestAdd() {
        try {
            testObject.addFile(null, null);
        }
        catch (IOException e) {
            Assert.fail();
        }
    }

    @Test(expected = NullPointerException.class)
    public void nullTestAddFolder() {
        try {
            testObject.addFolder(null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void nullTestGetFile() {
        try {
            testObject.getFile(null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void nulltestDelete() {
        try {
            testObject.delete(null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void nulltestRename() {
        try {
            testObject.rename(null, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void nullTestGoTo() {
        try {
            testObject.goTo(null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isBaseTest() {
        Assert.assertTrue(testObject.isBaseFolder());
        try {
            testObject.goTo("testDir");
            Assert.assertFalse(testObject.isBaseFolder());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void goToTest() {
        try {
            testObject.goTo("testDir");
            Assert.assertFalse(testObject.isBaseFolder());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void goUpTest() {
        try {
            testObject.goUp();
            Assert.assertTrue(testObject.isBaseFolder());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            testObject.goTo("test");
            testObject.goUp();
            Assert.assertTrue(testObject.isBaseFolder());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void goToBaseTest() {
        try {
            testObject.goTo("testDir");
            testObject.goTo("testDir2");
            testObject.goTo("testDir3");
            testObject.goTobaseDir();
            Assert.assertTrue(testObject.isBaseFolder());
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
