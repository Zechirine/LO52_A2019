LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

$(call inherit-product, Linaro/product/hikey.mk)

PRODUCT_NAME := lo52_KeepIt
PRODUCT_PROPERTY_OVERRIDES := \
	ro.hw=lo52		\
	net.dns1=8.8.8.8	\
	net.dns2=4.4.4.4	
PRODUCT_PACKAGES := libusb
