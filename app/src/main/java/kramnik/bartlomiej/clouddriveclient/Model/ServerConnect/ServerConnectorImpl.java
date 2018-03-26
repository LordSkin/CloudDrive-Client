package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

/**
 * Implementation of ServerConnector, alll methods are synchronous
 */

public class ServerConnectorImpl implements ServerConnector {

    private OkHttpClient client;
    private String baseAddress;
    Context context;

    public ServerConnectorImpl(Context context, String baseAddress) {
        if (!baseAddress.endsWith("/")){
            baseAddress+="/";
        }
        this.baseAddress = baseAddress;
        client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();
        this.context = context;
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
    public void getFile(String url, String name, ProgressIndicator indicator) throws IOException {

        Call call = client.newCall(new Request.Builder().url(baseAddress+"/get/"+url).get().build());

        Response response = call.execute();
        if (response.code() == 200 || response.code() == 201) {

            InputStream inputStream = response.body().byteStream();

            byte[] buff = new byte[1024 * 4];
            long downloaded = 0;
            long target = response.body().contentLength();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
            OutputStream output = new FileOutputStream(file);

            indicator.setProgress(0, target);
            while (true) {
                int readed = inputStream.read(buff);

                if (readed == -1) {
                    break;
                }
                output.write(buff, 0, readed);
                downloaded += readed;
                indicator.setProgress(downloaded, target);
            }

            output.flush();
            output.close();

            if (downloaded == target) {
                indicator.completed(file);
            }
            else {
                throw new IOException();
            }
        }
        else {
            throw new IOException("wrong response code");
        }
    }

    @Override
    public File getFile(String url, String name) throws IOException {
        Call call = client.newCall(new Request.Builder().url(baseAddress+"/get/"+url).get().build());

        Response response = call.execute();
        if (response.code() == 200 || response.code() == 201) {

            InputStream inputStream = response.body().byteStream();

            byte[] buff = new byte[1024 * 4];
            long downloaded = 0;
            long target = response.body().contentLength();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
            OutputStream output = new FileOutputStream(file);

            while (true) {
                int readed = inputStream.read(buff);

                if (readed == -1) {
                    break;
                }
                output.write(buff, 0, readed);
                downloaded += readed;
            }

            output.flush();
            output.close();

            if (downloaded == target) {
                return file;
            }
            else {
                throw new IOException();
            }
        }
        else {
            throw new IOException("wrong response code");
        }
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
