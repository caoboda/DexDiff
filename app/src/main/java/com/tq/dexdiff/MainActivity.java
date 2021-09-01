package com.tq.dexdiff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

/**
 * apk差量更新步骤：
 * 1、打包更新前的apk,并安装该apk到手机
 * 2、打包个更新后的apk
 * 3、使用 bsdiff oldfile newfile patchfile命令生成patch文件 (patch.apk) 具体见bsdiff命令.png
 * 4、把patch.apk上传到getExternalFilesDir路径下的apk文件中，即/sdcard/Android/data/com.tq.dexdiff/files/apk/patch.apk
 * 5、点击安装的apk的textview，则执行增量更新代码 实现安装的old.apk和patch.apk的合并后安装从而实现apk的更新。
 *
 */

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testDiff();
    }

    public void testDiff() {
        File file = copy4Assets();
        PathClassLoader classLoader = new PathClassLoader(file.getPath(), getClassLoader());
        try {
            Class<?> test = classLoader.loadClass("Test");
            Method main = test.getDeclaredMethod("test");
            //调用new.dex里面的test方法
            main.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File copy4Assets() {
        File externalFilesDir = getExternalFilesDir("");
        File dexFile = new File(externalFilesDir, "new.dex");
        if (!dexFile.exists()) {
            BufferedInputStream is = null;
            BufferedOutputStream os = null;
            try {
                is = new BufferedInputStream(getAssets().open("new.dex"));
                os = new BufferedOutputStream(new FileOutputStream(dexFile));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dexFile;
    }


    public void patch(View view) {
        //getExternalFilesDir 内置存储卡的私有目录兼容10.0的分区存储，且不需要读写权限
        File newFile = new File(getExternalFilesDir("apk"), "app.apk");
        File patchFile = new File(getExternalFilesDir("apk"), "patch.apk");
         /**
         *  sourceDir   Full path to the base APK for this application.即当前安装的apk的全路径
         */
        int result = BsPatchUtils.patch(getApplicationInfo().sourceDir, newFile.getAbsolutePath(),
                patchFile.getAbsolutePath());
        if (result == 0) {
            install(newFile);
        }
    }

    private void install(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
}
