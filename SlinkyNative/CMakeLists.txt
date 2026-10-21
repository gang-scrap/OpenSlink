add_library(SlinkyNative SHARED
    src/DllMain.cpp
    src/Logger.cpp
    src/JvmManager.cpp
    src/MinecraftDetector.cpp
    src/NativeBridge.cpp
    src/PayloadLoader.cpp
    SlinkyNative.rc
)

target_include_directories(SlinkyNative PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/include
    ${CMAKE_CURRENT_SOURCE_DIR}/include/jdk
    ${CMAKE_CURRENT_SOURCE_DIR}/include/jdk/win32
)

target_compile_definitions(SlinkyNative PRIVATE
    _CRT_SECURE_NO_WARNINGS
    WIN32_LEAN_AND_MEAN
    EMBED_PAYLOAD_JARS
)

set_target_properties(SlinkyNative PROPERTIES
    CXX_STANDARD 17
    CXX_STANDARD_REQUIRED ON
    OUTPUT_NAME "SlinkyNative"
)
