cmake_minimum_required(VERSION 3.4.1)
add_library(app-security SHARED src/main/cpp/app-security.c )
find_library(log-lib  log )
target_link_libraries(app-security ${log-lib} )