cmake_minimum_required(VERSION 3.1)
project(c_client)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -O0 -g")


set(SRC_DIR "src")
set(OBJ_DIR "bin")


set(SRC_1 ${SRC_DIR}/zad1/client.c)
set(SRC_2 ${SRC_DIR}/zad2/file_client.c)
set(UTIL ${SRC_DIR}/util)

include_directories("${UTIL}")

add_executable(c_client "${SRC_1}" "${UTIL}/net.c")
add_executable(file_client "${SRC_2}" "${UTIL}/net.c")