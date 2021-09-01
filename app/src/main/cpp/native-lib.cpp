#include <jni.h>
#include <string>
#include <android/log.h>

//兼容C语言
extern "C"{
    extern int executePatch(int argc, char * argv[]);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_tq_dexdiff_BsPatchUtils_patch(JNIEnv *env, jclass clazz, jstring old_apk, jstring new_apk,
                                       jstring patch_file) {
    const char* oldApk= env->GetStringUTFChars(old_apk, 0);
    const char* newApk= env->GetStringUTFChars(new_apk, 0);
    const char* patchFile= env->GetStringUTFChars(patch_file, 0);
    int args = 4;
    char *argv[args];
    argv[0] = "bspatch";
    argv[1] = const_cast<char *>(oldApk);
    argv[2] = const_cast<char *>(newApk);
    argv[3] = const_cast<char *>(patchFile);
    //此处executePathch()就是上面我们main方法修改出的
    int result= executePatch(args,argv);
    env->ReleaseStringUTFChars(old_apk, argv[1]);
    env->ReleaseStringUTFChars(new_apk, argv[2]);
    env->ReleaseStringUTFChars(patch_file, argv[3]);

    __android_log_print(ANDROID_LOG_ERROR,"diff ","==%s==%s==%s==%d",argv[1] ,argv[2] ,argv[3],result);
    return result;

}