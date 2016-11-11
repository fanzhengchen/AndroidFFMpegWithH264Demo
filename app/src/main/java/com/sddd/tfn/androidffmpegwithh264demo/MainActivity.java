package com.sddd.tfn.androidffmpegwithh264demo;

import android.Manifest;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button mTransBtn = null;
    private TextView mStartTxt = null;
    private TextView mEndTxt = null;
    private TextView mTotalTxt = null;
    private TextView mResultTxt = null;
    private RelativeLayout mWaitRL = null;

    private static final String TAG = "AAATFN";

    private String basePath = "/storage/emulated/0";
    private SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date curDate = new Date(System.currentTimeMillis());
    private String currentTime = sFormat.format(curDate);
    private SimpleDateFormat sFormat1 = new SimpleDateFormat("HH:mm:ss");
    private String startTime = "00:00:00";
    private String endTime = "00:00:00";
    private long startMill = 0L;
    private long endMill = 0L;
    private long totalTime = 0L;

    private String[] cmds = {
            "ffmpeg",
            "-i",
            basePath + File.separator + "video_20161111_164706.mp4",
            "-c:v",
            "libx264",
            basePath + File.separator + "out_" + currentTime + ".mp4"
    };

//    private String[] cmds = {
//            "ffmpeg",
//            "-i",
//            basePath + File.separator + "video_20161111_164706.mp4",
//            "-b",
//            "9600",
//            "-s",
//            "1080x720",
//            "-r", "24",
//            "-c:v",
//            "libx264",
//            basePath + File.separator + "out_" + currentTime + ".mp4"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                200);
        mTransBtn = (Button) findViewById(R.id.trans_btn);
        mTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transcodeBtnOnClick();
            }
        });

        mStartTxt = (TextView) findViewById(R.id.start_time_txt);
        mEndTxt = (TextView) findViewById(R.id.end_time_txt);
        mTotalTxt = (TextView) findViewById(R.id.total_time_txt);

        mResultTxt = (TextView) findViewById(R.id.result_txt);
        mResultTxt.setVisibility(View.INVISIBLE);

        mWaitRL = (RelativeLayout) findViewById(R.id.wait_rl);
        mWaitRL.setVisibility(View.INVISIBLE);
    }

    private void transcodeBtnOnClick() {
        new MyAsyncTask().execute(cmds);
    }

    class MyAsyncTask extends AsyncTask<String[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute()");
            startMill = System.currentTimeMillis();
            startTime = sFormat1.format(new Date(startMill));
            mStartTxt.setText(startTime);
            mWaitRL.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String[]... strings) {
            Log.d(TAG, "doInBackground()");
            return Player.transcodeVideo(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "onPostExecute(),result=" + integer);
            mWaitRL.setVisibility(View.INVISIBLE);
            endMill = System.currentTimeMillis();
            endTime = sFormat1.format(new Date(endMill));
            totalTime = (endMill - startMill) / 1000;
            mEndTxt.setText(endTime);
            mTotalTxt.setText(String.valueOf(totalTime));
            mResultTxt.setVisibility(View.VISIBLE);
            String resultStr = "转码成功！";
            if (integer != 0) {
                resultStr = "转码失败，返回值：" + integer;
            }
            mResultTxt.setText(resultStr);
            super.onPostExecute(integer);
        }
    }
}
