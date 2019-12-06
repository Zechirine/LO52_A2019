LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)


LOCAL_MODULE:=libusb
LOCAL_SRC_FILES:= \
	 core.c \
	 descriptor.c \
	 io.c \
	 sync.c \
	 os/linux_usbfs.c 

LOCAL_C_INCLUDES +=	/external/libusb/libusb.h \
					/external/libusb/libusbi.h \
					/external/libusb/os/linux_usbfs.h 


LOCAL_SHARED_LIBRARIES := libc libusb
LOCAL_MODULE_TAGS:= optional

include $(BUILD_SHARED_LIBRARY)