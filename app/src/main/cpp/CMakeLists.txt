cmake_minimum_required(VERSION 3.4.1)

aux_source_directory(bzip2 SOURCES)

add_library(
             bspatch_utils
             SHARED
             native-lib.cpp
             bspatch.c
             ${SOURCES})

find_library(
              log-lib
              log )

include_directories(bzip2)

target_link_libraries(
                       bspatch_utils
                       ${log-lib} )