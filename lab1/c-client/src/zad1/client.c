#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <stdint.h>
#include <stdint-gcc.h>
#include <stdlib.h>
#include "net.h"

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

    if(argc != 5) {
        printf("Expected 4 arguments: 1-, 2-, 4- and 8-byte numbers\n");
        exit(-1);
    }

    int one_byte = atoi(argv[1]);
    int two_byte = atoi(argv[2]);
    int four_byte = atoi(argv[3]);
    long long int eight_byte = atoll(argv[4]);

    fd = create_and_connect_socket("127.0.0.1", 22333);
    compose_message(one_byte, two_byte, four_byte, eight_byte, sendline);

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
