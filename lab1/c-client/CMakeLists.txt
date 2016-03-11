cmake_minimum_required(VERSION 3.1)
project(c_client)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")


set(SRC_DIR "src")
set(OBJ_DIR "bin")

include_directories(${SRC_DIR}/zad1)

set(SRC_1 ${SRC_DIR}/zad1/client.c)

add_executable(c_client ${SRC_1})