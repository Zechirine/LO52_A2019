$(call inherit-product, device/linaro/hikey.mk)

PRODUCT_PACKAGES += libusb
PRODUCT_PROPERTY_OVERRIDES += ro.hw=lo52 \
							  net.dns1=8.8.8.8 \
							  net.dns2=4.4.4.4
PRODUCT_PACKAGE_OVERLAYS := device/utbm/lo52_fail/overlay

PRODUCT_NAME := lo52_fail
PRODUCT_DEVICE := lo52_fail
PRODUCT_BRAND := lo52_fail
PRODUCT_MODEL := lo52_fail

include $(call all-subdir-makefiles)