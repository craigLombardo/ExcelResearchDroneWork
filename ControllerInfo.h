#include <jni.h>

#ifndef _Included_ControllerInfo
#define _Included_ControllerInfo

#ifdef __cplusplus
extern "C"{
endif

	JNIEXPORT int JNICALL Java_ControllerInfo_getRoll(JNIEnv *, jobject);
	JNIEXPORT int JNICALL Java_ControllerInfo_getPitch(JNIEnv *, jobject);
	JNIEXPORT int JNICALL Java_ControllerInfo_getThrottle(JNIEnv *, jobject);
	JNIEXPORT int JNICALL Java_ControllerInfo_getYaw(JNIEnv *, jobject);
	JNIEXPORT int JNICALL Java_ControllerInfo_connect(JNIEnv *, jobject);
	JNIEXPORT int JNICALL Java_ControllerInfo_update(JNIEnv *, jobject);
	
#ifdef __cplusplus
}
#endif
#endif
