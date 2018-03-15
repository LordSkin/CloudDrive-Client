package kramnik.bartlomiej.clouddriveclient.Model;

import android.accounts.NetworkErrorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServerDataBase;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersDao;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;

/**
 * tests of ServersList
 */

public class ServersListTests {

    ServersList testObject;

    @Before
    public void prepare() {

        ServerDataBase dataBase = Mockito.mock(ServerDataBase.class);
        ServersDao dao = Mockito.mock(ServersDao.class);
        Mockito.when(dataBase.serversDao()).thenReturn(dao);

        Mockito.when(dao.getAll()).thenReturn(new ArrayList<ServerEntity>());

        testObject = new ServersList(dataBase);
    }

    @Test(expected = NullPointerException.class)
    public void addNullTest() {
        testObject.addServer(null);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullTest() {
        testObject.deleteServer(null);
    }

    @Test
    public void addTest() {
        testObject.addServer(new ServerEntity("name", "ip"));
        testObject.addServer(new ServerEntity("name", "ip"));

        Assert.assertEquals(2, testObject.getCount());
    }

    @Test
    public void deleteTest() {
        testObject.addServer(new ServerEntity("name", "ip"));
        testObject.addServer(new ServerEntity("name", "ip"));

        testObject.deleteServer(1);
        testObject.deleteServer(0);

        Assert.assertEquals(0, testObject.getCount());
    }

    @Test
    public void getAllTest() {
        ServerEntity testServer = new ServerEntity("test", "ip");

        testObject.addServer(testServer);

        Assert.assertEquals(testServer, testObject.getServers().get(0));
        Assert.assertEquals(1, testObject.getServers().size());
    }


}
