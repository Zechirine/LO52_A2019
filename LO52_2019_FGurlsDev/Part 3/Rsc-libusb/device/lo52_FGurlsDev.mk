$(call inherite-product,device/Linaro/hikey/hikey.mk)
PRODUCT_PROPERTY_OVERRIDES+= \
	ro.hw=lo52 \
	net.dns1=8.8.8.8 \
	net.dns2=4.4.4.4

PRODUCT_NAME:=lo52_FGurlsDev
PRODUCT_DEVICE:=lo52_FGurlsDev
PRODUCT_BRAND:=lo52_FGurlsDev
PRODUCT_MODEL:=lo52_FGurlsDev

DEVICE_PACKAGE_OVERLAYS:=overlay

PRODUCT_PACKAGES+=libusb

include $(call all-subdir-makefiles)