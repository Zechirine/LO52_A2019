#include <jni.h>
#include <string>
// public native void left();
//    public native void right();
//    public native void down();
//    public native void up();
//    public native void write();
//    public native void read();
extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_lo52_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_utbm_lo52_MainActivity_read(
        JNIEnv *env,
        jobject /* this */,
        jint number) {
    return number*number;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_utbm_lo52_MainActivity_write(
        JNIEnv *env,
        jobject /* this */,
        jint number) {
    return number*number*number;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_lo52_MainActivity_left(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "links";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_lo52_MainActivity_right(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "recht";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_lo52_MainActivity_up(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "oben";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_lo52_MainActivity_down(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "nieder";
    return env->NewStringUTF(hello.c_str());
}