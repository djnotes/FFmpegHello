package me.mehdi.ffmpegsample;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private Button run;
    private EditText command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        command = findViewById(R.id.command);
        run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFFmpeg(new String[] {command.getText().toString()});
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
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    AlertDialog dialog = new AlertDialog.Builder(mContext).setMessage(message).setPositiveButton("OK", null).create();
                    dialog.show();
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
