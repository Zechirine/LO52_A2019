# Installation sur Mac

Installer homebrew disponible à cette adresse [https://brew.sh/index_fr](https://brew.sh/index_fr)

```bash
brew install repo
brew install libelf

mkdir android
cd android

# Récupérer les sources
repo init -u https://android.googlesource.com/kernel/manifest -b hikey-linaro-android-4.19
repo sync -f -j 2

# Récupérer la toolchain
git clone https://android.googlesource.com/platform/prebuilts/gcc/darwin-x86/aarch64/aarch64-linux-android-4.9

```

L'abandon est réel, les headers ne sont pas venus.