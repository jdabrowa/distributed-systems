#include "net.h"

int create_and_connect_socket(const char *addr, int port_no) {
    struct sockaddr_in serv_addr;
    int fd = socket(AF_INET, SOCK_STREAM, 0);
    bzero((char*)&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr(addr);
    serv_addr.sin_port = htons((uint16_t) port_no);
    connect(fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr));
    return fd;
}

uint32_t read_int(int socket_fd) {
    char resp_buff[4];
    int counter = 0;
    while(counter < 4) {
        int len = (int) recv(socket_fd, resp_buff+counter, 4-counter, 0);
        counter += len;
    }
    return ntohl(*((uint32_t*)resp_buff));
}