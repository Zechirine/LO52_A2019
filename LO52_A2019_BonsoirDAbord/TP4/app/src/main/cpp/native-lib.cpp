#include <jni.h>
#include <string>
#include <sstream>

static void setLabelText(JNIEnv *env, jobject dis, const char *str)
{
    jclass clazz = env->GetObjectClass(dis);
    jfieldID labelField = env->GetFieldID(clazz, "label", "Landroid/widget/TextView;");
    jobject label = env->GetObjectField(dis, labelField);

    clazz = env->GetObjectClass(label);
    jmethodID setTextMethod = env->GetMethodID(clazz, "setText", "(Ljava/lang/CharSequence;)V");

    jstring jstr = env->NewStringUTF(str);
    env->CallVoidMethod(label, setTextMethod, jstr);
}

extern "C" JNIEXPORT void JNICALL Java_org_utbm_lo52_bonsoirdabord_tp4_MainActivity_callNativeRead(JNIEnv* env, jobject dis, jint nbr)
{
    std::ostringstream oss;
    oss << "READ : " << nbr * nbr;

    std::string str(oss.str());
    setLabelText(env, dis, str.c_str());
}

extern "C" JNIEXPORT void JNICALL Java_org_utbm_lo52_bonsoirdabord_tp4_MainActivity_callNativeWrite(JNIEnv* env, jobject dis, jint nbr)
{
    std::ostringstream oss;
    oss << "READ : " << nbr * nbr * nbr;

    std::string str(oss.str());
    setLabelText(env, dis, str.c_str());
}

extern "C" JNIEXPORT void JNICALL Java_org_utbm_lo52_bonsoirdabord_tp4_MainActivity_callNativeButton(JNIEnv* env, jobject dis, jstring jstr)
{
    jsize strLen = env->GetStringUTFLength(jstr);
    const char *cstr = env->GetStringUTFChars(jstr, NULL);
    std::string str(cstr, strLen);
    env->ReleaseStringUTFChars(jstr, cstr);

    const char *result;
    if(str == "LEFT")
        result = "LINKS";
    else if(str == "RIGHT")
        result = "RECHTS";
    else if(str == "UP")
        result = "HOCH";
    else if(str == "DOWN")
        result = "RUNTER";
    else
        result = "WAS IST DAS FUR EINE SCHEISE";

    setLabelText(env, dis, result);
}
