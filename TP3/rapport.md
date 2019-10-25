# Implémentation de la libusb

https://github.com/spark85/libusb1-android
https://android.serverbox.ch/?p=151

## Corriger TIMESPEC_TO_TIMEVAL

À ajouter dans libusb/io.c.

```c
#define TIMESPEC_TO_TIMEVAL(tv, ts)                                     \
        do {                                                            \
                (tv)->tv_sec = (ts)->tv_sec;                            \
                (tv)->tv_usec = (ts)->tv_nsec / 1000;                   \
        } while (0)
```

## Corriger erreur de link

build/tools/apriori/prelinkmap.c(137): library ‘libusb.so’ not in prelink map

Ajouter dans ./build/core/prelink-linux-arm.map

```
libqcamera.so       0xA9400000
libusb.so           0xA8000000
```

# Implémentation d'un nouveau produit Android