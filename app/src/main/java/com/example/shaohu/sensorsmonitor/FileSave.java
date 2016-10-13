package com.example.shaohu.sensorsmonitor;

/**
 * Created by Shaohu on 10/12/2016.
 */

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Shaohu on 10/9/2016.
 */

public class FileSave {
    private File mFile = null;
    private  String mLog = "";
    private  boolean mbFlash = true;
    private int mCount = 0;



    public FileSave(String FileName, boolean bFlush)
    {
        mbFlash = bFlush;

        try {
            mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FileName);
            if(mFile.exists())
                mFile.delete();

            mFile.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendLog(String text) {
        mLog = mLog + text;
        // mLog = mLog + System.getProperty("line.separator");
        if (mbFlash || mCount == 20) {
            mCount = 0;
            flushToLog();
        }

        if (!mbFlash)
            mCount++;
    }

    public void flushToLog()
    {
        if (mLog == "")
            return;

        FileOutputStream outputStream;
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(mFile, true));
            buf.append(mLog);
            //buf.newLine();
            buf.close();
            mLog = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

