#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {
#include "ffmpeg.h"
}

const int SIZE = 1 << 9;

#define  LOG_TAG    "videoplayer"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C"
jstring
Java_com_sddd_tfn_androidffmpegwithh264demo_Player_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_sddd_tfn_androidffmpegwithh264demo_Player_transcodeVideo(JNIEnv *env, jclass type,
                                                              jobjectArray cmds) {

    jint argc = env->GetArrayLength(cmds);
    jboolean copy = false;

    char **argv = (char **) malloc(sizeof(char *) * argc);
    for (int i = 0; i < argc; ++i) {
        jstring str = (jstring) (env->GetObjectArrayElement(cmds, i));
        const char *s = env->GetStringUTFChars(str, &copy);
        argv[i] = (char *) malloc(sizeof(char) * SIZE);
        strcpy(argv[i], s);
        LOGD("command %s", argv[i]);
    }
    int ret = run(argc, argv);
    free(argv);
    LOGD("transcode ret %d", ret);
    return ret;
}
