# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

# CMAKE 3.12+ 支持C++ 20 https://cmake.org/cmake/help/v3.12/prop_tgt/CXX_STANDARD.html
cmake_minimum_required(VERSION 3.4.1)

# Declares and names the project.
project("app")

ENABLE_LANGUAGE(ASM)

add_definitions(-std=c++11)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

set(${PROJECT_NAME}_SOURCES
        sandhook.cpp
        trampoline/trampoline.cpp
        trampoline/trampoline_manager.cpp
        utils/dlfcn_nougat.cpp
        utils/hide_api.cpp
        utils/utils.cpp
        utils/offset.cpp
        utils/elf_util.cpp
        casts/cast_art_method.cpp
        casts/cast_compiler_options.cpp
        art/art_method.cpp
        art/art_compiler_options.cpp
        trampoline/arch/arm32.S
        trampoline/arch/arm64.S
        inst/insts_arm32.cpp
        inst/insts_arm64.cpp
        nativehook/native_hook.cpp
        )

add_library( # Sets the name of the library.
        app

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        ${${PROJECT_NAME}_SOURCES}
        )

include_directories(
        .
        ../../../../nativehook/src/main/cpp
)

add_library(sandhook-native
        SHARED
        IMPORTED)

set_target_properties(sandhook-native
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libsandhook-native.so)


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        app

        sandhook-native
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})