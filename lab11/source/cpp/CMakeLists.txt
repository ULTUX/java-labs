cmake_minimum_required(VERSION 3.21)
project(JAVJ)

set(CMAKE_CXX_STANDARD 20)
find_package(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})
add_library(JAVJ SHARED library.cpp)
find_package(unofficial-nana CONFIG REQUIRED)
target_link_libraries(JAVJ PRIVATE unofficial::nana::nana)
find_package(JPEG REQUIRED)
target_link_libraries(JAVJ PRIVATE ${JPEG_LIBRARIES})
target_include_directories(JAVJ PRIVATE ${JPEG_INCLUDE_DIR})