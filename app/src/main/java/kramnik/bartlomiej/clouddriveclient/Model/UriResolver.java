package kramnik.bartlomiej.clouddriveclient.Model;

import android.content.Context;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * REsolving document Uri to File
 */

public class UriResolver {

    private Context context;

    public UriResolver(Context context) {
        this.context = context;
    }

    public File getFile(Uri uri){

        try{
            String filePath =  context.getCacheDir().getPath() + "file";

            File file = new File(filePath);

            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            InputStream iStream = context.getContentResolver().openInputStream(uri);
            byte[] inputData = getBytes(iStream);
            writeFile(inputData,filePath);

            return new File(filePath);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;

        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }


    public void writeFile(byte[] data, String fileName) throws IOException{
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

}
