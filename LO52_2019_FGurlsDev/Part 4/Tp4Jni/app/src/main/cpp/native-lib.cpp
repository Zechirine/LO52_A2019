#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_fr_utbm_fgurlsdev_tp4jni_NDKTools_stringFromJNI(
        JNIEnv *env,
        jclass /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_fr_utbm_fgurlsdev_tp4jni_NDKTools_sayHi(
        JNIEnv *env,
        jclass /* this */) {
    std::string hello = "Hi! We are FGurlsDev!";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_fr_utbm_fgurlsdev_tp4jni_NDKTools_readBtn(
        JNIEnv *env,
        jclass /* this */,
        jstring numStr) {
    const char *nativeString = env->GetStringUTFChars(numStr, JNI_FALSE);
    char *strTemp = (char *) malloc(12 + 2);
    strcpy(strTemp, "READ : ");
    strcat(strTemp, nativeString);
    strcat(strTemp, " * ");
    strcat(strTemp, nativeString);
    // Free space in JVM
    env->ReleaseStringUTFChars(numStr, nativeString);
    return env->NewStringUTF(strTemp);
}

extern "C" JNIEXPORT jstring JNICALL
Java_fr_utbm_fgurlsdev_tp4jni_NDKTools_writeBtn(
        JNIEnv *env,
        jclass /* this */,
        jstring numStr) {
    const char *nativeString = env->GetStringUTFChars(numStr, JNI_FALSE);
    char *strTemp = (char *) malloc(16 + 3);
    strcpy(strTemp, "READ : ");
    strcat(strTemp, nativeString);
    strcat(strTemp, " * ");
    strcat(strTemp, nativeString);
    strcat(strTemp, " * ");
    strcat(strTemp, nativeString);
    env->ReleaseStringUTFChars(numStr, nativeString);
    return env->NewStringUTF(strTemp);
}

extern "C" JNIEXPORT jstring JNICALL
Java_fr_utbm_fgurlsdev_tp4jni_NDKTools_directionBtn(
        JNIEnv *env,
        jclass /* this */,
        jstring direction) {
    const char *nativeString = env->GetStringUTFChars(direction, JNI_FALSE);
    if (strcmp(nativeString, "UP") == 0) return env->NewStringUTF("Oben");
    if (strcmp(nativeString, "DOWN") == 0) return env->NewStringUTF("Unten");
    if (strcmp(nativeString, "LEFT") == 0) return env->NewStringUTF("Links");
    if (strcmp(nativeString, "RIGHT") == 0) return env->NewStringUTF("Rechts");
    env->ReleaseStringUTFChars(direction, nativeString);
    return env->NewStringUTF("No such direction.");
}