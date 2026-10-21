add_executable(Slinky
    src/Main.cpp
    src/Injector.cpp
    src/ProcessUtil.cpp
    src/ConsoleUI.cpp
    Slinky.rc
)

target_include_directories(Slinky PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/include
)

target_compile_definitions(Slinky PRIVATE
    _CRT_SECURE_NO_WARNINGS
    WIN32_LEAN_AND_MEAN
)

set_target_properties(Slinky PROPERTIES
    CXX_STANDARD 17
    CXX_STANDARD_REQUIRED ON
    OUTPUT_NAME "Slinky"
)
