#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_utbm_tp4_1jni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jint JNICALL
Java_com_utbm_tp4_1jni_MainActivity_squareFromJNI(
        JNIEnv *env,
    jobject , jint a) {

return (a*a);
}
extern "C" JNIEXPORT jint JNICALL
Java_com_utbm_tp4_1jni_MainActivity_cubedFromJNI(
        JNIEnv *env,
        jobject , jint a) {

    return (a*a*a);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_utbm_tp4_1jni_MainActivity_linksFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement linksFromJNI()
    std::string hello = "links";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_utbm_tp4_1jni_MainActivity_rechtFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement rechtFromJNI()
    std::string hello = "recht";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_utbm_tp4_1jni_MainActivity_niederFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement niederFromJNI()
    std::string hello = "nieder";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_utbm_tp4_1jni_MainActivity_obenFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement obenFromJNI()
    std::string hello = "oben";
    return env->NewStringUTF(hello.c_str());
}