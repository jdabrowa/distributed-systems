#ifndef C_CLIENT_NET_H
#define C_CLIENT_NET_H

#include <sys/socket.h>
#include <stdint.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <strings.h>

int create_and_connect_socket(const char *, int);

#endif //C_CLIENT_NET_H
