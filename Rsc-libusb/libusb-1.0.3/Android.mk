LOCAL_PATH := $(call my-dir)
ubdirs := $(addprefix $(LOCAL_PATH)/,$(addsuffix /Android.mk, \
		libusb \
))
include $(subdirs)