package com.sddd.tfn.androidffmpegwithh264demo;

/**
 * Created by tfn on 16-11-11.
 */

public class Player {
    static {
        System.loadLibrary("VideoPlayer");
    }

    public static native int transcodeVideo(String[] cmds);
}
