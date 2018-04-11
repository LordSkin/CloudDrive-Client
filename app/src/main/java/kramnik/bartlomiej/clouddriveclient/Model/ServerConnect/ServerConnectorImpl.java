package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import android.content.Context;
import android.net.wifi.hotspot2.pps.Credential;
import android.support.annotation.Nullable;

import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import okhttp3.Authenticator;
import okhttp3.Challenge;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Implementation of ServerConnector, alll methods are synchronous
 */

public class ServerConnectorImpl implements ServerConnector {

    private OkHttpClient client;
    private String baseAddress;
    private Context context;
    private RxDownloader downloader;

    public ServerConnectorImpl(Context context, String baseAddress) {
        if (!baseAddress.endsWith("/")){
            baseAddress+="/";
        }
        this.baseAddress = baseAddress;
        client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();
        this.context = context;
        this.downloader = new RxDownloader(context);
    }

    public ServerConnectorImpl(String baseAddress) {
        this.baseAddress = baseAddress;
        client = new OkHttpClient();
    }

    @Override
    public String getBaseAddress() {
        return baseAddress;
    }

    @Override
    public String getFileToken(String filePath) throws IOException {
        Request request = new Request.Builder()
                .url(baseAddress+"/token/"+filePath)
                .build();

        Response response = client.newCall(request).execute();
        String s = response.body().string();
        return s;
    }


    @Override
    public List<FileDetails> getList(String url, JsonConverter converter) throws IOException {
        Request request = new Request.Builder()
                .url(baseAddress+"/list/"+url)
                .build();

        Response response = client.newCall(request).execute();
        String s = response.body().string();
        return converter.getFilesList(s);
    }

    @Override
    public boolean setPassword(final String userName, final String password) {
        OkHttpClient newClient = new OkHttpClient
                .Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(userName, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();

        try {
            Request request = new Request.Builder()
                    .url(baseAddress+"/list/")
                    .build();
            Response response = newClient.newCall(request).execute();

            if(response.code()!=401){
                client = newClient;
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(baseAddress+url)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        return response.code()==200||response.code()==201;
    }

    @Override
    public boolean rename(String url, String newName) throws IOException {
        Request request = new Request.Builder()
                .url(baseAddress+"rename/"+url+"/"+newName)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.code()==200||response.code()==201;
    }

    @Override
    public boolean addFile(File file, String url) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "file", RequestBody.create(MediaType.parse("other"), file))
                .build();
        Request request = new Request.Builder().url(baseAddress+url).post(requestBody).build();

        Response response = client.newCall(request).execute();

        return response.code()==200||response.code()==201;
    }

    @Override
    public boolean addFolder(String url) throws IOException {
        Request request = new Request.Builder()
                .url(baseAddress+"/folder/"+url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.code()==200||response.code()==201;
    }

    @Override
    public boolean ping() {
        try{
            Request request = new Request.Builder()
                    .url(baseAddress)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            return response.code()==200||response.code()==201;
        }
        catch (Exception e){
            return false;
        }

    }

}
