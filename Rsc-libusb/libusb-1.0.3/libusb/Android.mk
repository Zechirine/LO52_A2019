LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := core.c descriptor.c io.c os/darwin_usb.c os/linux_usbfs.c
LOCAL_C_INCLUDES += $(LOCAL_PATH) $(LOCAL_PATH)/os
LOCAL_MODULES := libusb
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)
