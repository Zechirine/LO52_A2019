LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

$(call inherit-product, device/Linaro/hikey.mk)
PRODUCT_NAME := lo52_SandraJaiFroid
PRODUCT_DEVICE := lo52_SandraJaiFroid
PRODUCT_PROPERTY_OVERRIDES := \
    ro.hw=lo_52 \
    net.dns1=8.8.8.8 \
    net.dns2=4.4.4.4
PRODUCT_PACKAGES:= libusb
PRODUCT_PACKAGE_OVERLAYS := overlay/


