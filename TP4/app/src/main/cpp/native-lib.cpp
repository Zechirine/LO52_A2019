#include <iostream>
#include <jni.h>
#include <random>
#include <string>

float get_random(int min, int max) {
  static std::default_random_engine e;
  static std::uniform_real_distribution<> dis(min, max); // rage 0 - 1
  return dis(e);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_e_testjni_MainActivity_READ(
        JNIEnv* env,
        jobject obj) {
    auto a = int(get_random(-1, 11));
    std::string label = std::to_string(a * a);
    return env->NewStringUTF(label.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_e_testjni_MainActivity_WRITE(
        JNIEnv* env,
        jobject obj) {
    auto a = int(get_random(-1, 11));
    std::string label = std::to_string(a * a * a);
    return env->NewStringUTF(label.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_e_testjni_MainActivity_ToGerman(
        JNIEnv* env,
        jobject obj,
        jstring text) {

    std::string label = "Def";
    const char *nativeString = env->GetStringUTFChars(text, 0);
    std::string strLabel(nativeString);

    if (strLabel == "Up"){
        label = "Oben";
    } else if (strLabel == "Down"){
        label = "Nieder";
    } else if (strLabel == "Left"){
        label = "Links";
    } else if (strLabel == "Right"){
        label = "Recht";
    }

    env->ReleaseStringUTFChars(text, nativeString);
    return env->NewStringUTF(label.c_str());
}
