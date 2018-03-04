package me.mehdi.ffmpegsample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private Context mContext = this;
    private Button run;
    private EditText command;
    private TextView mResult;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Handle permissions
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        command = findViewById(R.id.command);
        run = findViewById(R.id.run);
        mResult = findViewById(R.id.result);
        mDialog = new AlertDialog.Builder(mContext).create();

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFFmpeg(command.getText().toString().split(" "));
            }
        });
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }



         //Available methods: http://writingminds.github.io/ffmpeg-android-java/

//        loadBinary(FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler) throws FFmpegNotSupportedException
//        execute(Map<String, String> environvenmentVars, String cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException
//        execute(String cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException
//        getDeviceFFmpegVersion() throws FFmpegCommandAlreadyRunningException
//        getLibraryFFmpegVersion()
//        isFFmpegCommandRunning()
//        killRunningProcesses()
//        setTimeout(long timeout)

    }

    private void runFFmpeg(String [] commands) {
        FFmpeg ffmpeg = FFmpeg.getInstance(MainActivity.this);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {


                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    String old = mResult.getText().toString();
                    mResult.setText(String.format("%s\n %s", old, message) );
                }

                @Override
                public void onFailure(String message) {
                    mDialog.setMessage(message);
                    mDialog.show();
                }

                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    mDialog.setMessage(message);
                    mDialog.show();
                }


                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }


    }
}
