/*This code was modeled after the work of Erik <br0ke@math.smsu.edu>
 * as it was sent To: linuxgames@sunsite.auc.dk on 
 * Date: Wed, 18 Aug 1999 00:41:09 -0500 (CDT).
 * 
 * Found on http://archives.seul.org/linuxgames/Aug-1999/msg00107.html
 * 
 */
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <linux/joystick.h>
#include <jni.h>

#define JOY_DEV "/dev/input/js0"
int roll;
int pitch;
int throttle;
int yaw;
int joy_fd, *axis, num_of_axis=0;
struct js_event js;

JNIEXPORT int JNICALL Java_ControllerInfo_getRoll(JNIEnv * env, jobject jobj){
 return roll;
}

JNIEXPORT int JNICALL Java_ControllerInfo_getPitch(JNIEnv * env, jobject jobj){
 return pitch;
}

JNIEXPORT int JNICALL Java_ControllerInfo_getThrottle(JNIEnv * env, jobject jobj){
 return throttle;
}

JNIEXPORT int JNICALL Java_ControllerInfo_getYaw(JNIEnv * env, jobject jobj){
 return yaw;
}

JNIEXPORT int JNICALL Java_ControllerInfo_connect(JNIEnv * env, jobject jobj){
  if((joy_fd = open(JOY_DEV,O_RDONLY)) < 0){
    printf("Oops, I wasn't able to open the device: \"%s\"",JOY_DEV);
    return -1;
  }
  
  printf("Device \"%s\" was opened successfully!\n",JOY_DEV);
 
  ioctl(joy_fd, JSIOCGAXES, &num_of_axis);
  
  axis = (int *) calloc(5,sizeof(int));
   return 0;
}

JNIEXPORT int JNICALL Java_ControllerInfo_update(JNIEnv * env, jobject jobj){
 struct stat st;
 fcntl( joy_fd, F_SETFL, O_NONBLOCK );
     read(joy_fd, &js, sizeof(js));
     axis[js.number] = js.value;
    
     roll=axis[0];
     pitch=axis[1];
     throttle=axis[2];
     yaw=axis[4];
     if (stat(JOY_DEV, &st) < 0){
        printf("\nOh no! Connection with the controller was lost...exiting\n");
        return -1;
     }
}
