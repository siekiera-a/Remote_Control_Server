#include <jni.h>
#include <windows.h>
#include <stdlib.h>
#include "MediaKeys.h"

#define KEYEVENTF_KEYDOWN 0

INPUT *generate_keyboard_input(WORD VK) {
	INPUT *input = (INPUT*)calloc(1, sizeof(INPUT));
	input->type = INPUT_KEYBOARD;
	input->ki.wScan = 0;
	input->ki.time = 0;
	input->ki.dwExtraInfo = 0;
	input->ki.wVk = VK;
	input->ki.dwFlags = KEYEVENTF_KEYDOWN;
	return input;
}

void click(WORD VK) {
	// create keydown and keyup input
	INPUT *press_key = generate_keyboard_input(VK);
	INPUT up_key = *press_key;
	up_key.ki.dwFlags = KEYEVENTF_KEYUP;

	const BYTE size = 2;
	INPUT sequence[] = { *press_key, up_key };
	SendInput(size, sequence, sizeof(INPUT));

	free(press_key);
	press_key = NULL;
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_volumeUp(JNIEnv *env, jobject obj) {
	click(VK_VOLUME_UP);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_VolumeDown(JNIEnv *env, jobject obj) {
	click(VK_VOLUME_DOWN);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_mute(JNIEnv *env, jobject obj) {
	click(VK_VOLUME_MUTE);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_mediaNext(JNIEnv *env, jobject obj) {
	click(VK_MEDIA_NEXT_TRACK);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_mediaPrevious(JNIEnv *env, jobject obj) {
	click(VK_MEDIA_PREV_TRACK);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_mediaPlayPause(JNIEnv *env, jobject obj) {
	click(VK_MEDIA_PLAY_PAUSE);
}

JNIEXPORT void JNICALL Java_org_remoteserver_MediaKeys_mediaStop(JNIEnv *env, jobject obj) {
	click(VK_MEDIA_STOP);
}
