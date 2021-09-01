package com.tq.dexdiff;

/**
 * Created by cbd on 2021/8/16
 * 合并差分包与老apk到新的apk
 */
public class BsPatchUtils {

    static  {
        System.loadLibrary("bspatch_utils");
    }

    //通过Jni调用bspatch.c中main方法(改名为executePatch)合并旧apk、patch为新的apk
    public static native int patch(String oldApk, String newApk,String patchFile);

}

