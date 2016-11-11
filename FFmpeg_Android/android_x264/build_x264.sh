export NDK=/home/tfn/Android/Sdk/ndk-bundle
export PREBUILT=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64
export PLATFORM=$NDK/platforms/android-16/arch-arm
export PREFIX=/home/tfn/FFmpeg_Android/android_x264/h264
export DIR=/home/tfn/FFmpeg_Android/android_x264/x264-snapshot-20161106-2245-stable

$DIR/configure --prefix=$PREFIX \
--enable-static \
--disable-shared \
--enable-pic \
--disable-asm \
--disable-cli \
--host=arm-linux \
--cross-prefix=$PREBUILT/bin/arm-linux-androideabi- \
--sysroot=$PLATFORM

make
make install
ldconfig
