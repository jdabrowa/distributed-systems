#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <strings.h>

static uint64_t htonll(uint64_t hll) {
    unsigned char out[8];
    out[0] = (unsigned char) ((hll >> 56) & 0x00000000000000FF);
    out[1] = (unsigned char) ((hll >> 48) & 0x00000000000000FF);
    out[2] = (unsigned char) ((hll >> 40) & 0x00000000000000FF);
    out[3] = (unsigned char) ((hll >> 32) & 0x00000000000000FF);
    out[4] = (unsigned char) ((hll >> 24) & 0x00000000000000FF);
    out[5] = (unsigned char) ((hll >> 16) & 0x00000000000000FF);
    out[6] = (unsigned char) ((hll >> 8) & 0x00000000000000FF);
    out[7] = (unsigned char) ((hll) & 0x00000000000000FF);
    return *(uint64_t*)out;
}

static void compose_message(u_int8_t, u_int16_t, u_int32_t, u_int64_t, char *);

int create_and_connect_socket(const char *, int);

int main(int argc, char *argv[]) {

    int fd, len;
    char recvline[4];
    char sendline[1+2+4+8];

    fd = create_and_connect_socket("127.0.0.1", 22333);
    compose_message(-1, -2, -3, -4, sendline);

    len = (int) send(fd, sendline, 15, 0);
    len = (int) recv(fd, recvline, 4, 0);
    printf("Received %d bytes: %d\n", len, ntohl(*((uint32_t*)recvline)));
    close(fd);
}

void compose_message(u_int8_t one_byte, u_int16_t two_bytes, u_int32_t four_bytes, u_int64_t eight_bytes, char *buff) {

    *(buff) = (char) ((u_int8_t) one_byte);
    *((uint16_t*) (buff+1)) = htons((uint16_t) two_bytes);
    *((uint32_t*) (buff+3)) = (uint32_t) htonl(four_bytes);
    *((uint64_t*) (buff+7)) = (uint64_t) htonll((uint64_t) eight_bytes);
}

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
