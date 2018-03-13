package kramnik.bartlomiej.clouddriveclient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

public class SelectDriveActivity extends AppCompatActivity implements View.OnClickListener {


    Observable<File> observable;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drive);
        final ServerConnector serverConnector = new ServerConnectorImpl(this);
        imageView = (ImageView)findViewById(R.id.image);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        observable = new Observable<File>() {
            @Override
            protected void subscribeActual(Observer<? super File> observer) {
                try{
                    File file = serverConnector.getFile("http:\\\\10.0.2.2:8080/get/photo.jpg", "photo.jpg");
                    serverConnector.addFile(file, "http:\\\\10.0.2.2:8080/dodane1.jpg");
                    observer.onNext(file);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        };





    }

    @Override
    public void onClick(View view) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageView.setImageBitmap(myBitmap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
