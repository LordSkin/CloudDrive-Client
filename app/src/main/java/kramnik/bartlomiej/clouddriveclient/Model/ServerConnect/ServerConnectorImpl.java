package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
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
    Context context;

    public ServerConnectorImpl(Context context) {
        client = new OkHttpClient();
        this.context = context;
    }

    public ServerConnectorImpl() {
        client = new OkHttpClient();
    }

    @Override
    public void getFile(String url, String name, ProgressIndicator indicator) throws IOException {
        Call call = client.newCall(new Request.Builder().url(url).get().build());

        Response response = call.execute();
        if (response.code() == 200 || response.code() == 201) {

            InputStream inputStream = response.body().byteStream();

            byte[] buff = new byte[1024 * 4];
            long downloaded = 0;
            long target = response.body().contentLength();
            File file = new File(context.getCacheDir(), name);
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
        Call call = client.newCall(new Request.Builder().url(url).get().build());

        Response response = call.execute();
        if (response.code() == 200 || response.code() == 201) {

            InputStream inputStream = response.body().byteStream();

            byte[] buff = new byte[1024 * 4];
            long downloaded = 0;
            long target = response.body().contentLength();
            File file = new File(context.getCacheDir(), name);
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
    public List<FileDetails> getList(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String s = response.body().string();
        return null;
    }

    @Override
    public boolean delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        return response.code()==200||response.code()==201;
    }

    @Override
    public boolean rename(String url, String newName) {
        // TODO: 11.03.2018  
        return false;
    }

    @Override
    public boolean addFile(File file, String url) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "file", RequestBody.create(MediaType.parse("other"), file))
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();

        Response response = client.newCall(request).execute();

        return response.code()==200||response.code()==201;
    }

}
