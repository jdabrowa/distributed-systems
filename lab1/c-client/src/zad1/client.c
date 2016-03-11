#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <strings.h>

uint64_t htonll(uint64_t hll) {
    unsigned char out[8] = {hll>>56,hll>>48,hll>>40,hll>>32,hll>>24,hll>>16,hll>>8,hll};
    return *(uint64_t*)out;
}

int main(int argc, char *argv[]) {

    struct sockaddr_in serv_addr;
    int fd, len;
    char recvline[4];
    char sendline[1+2+4+8];

    *(sendline) = ((u_int8_t) 1);
    *((uint16_t*) (sendline+1)) = htons(2);
    *((uint32_t*) (sendline+3)) = (uint32_t) htonl(99);
    *((uint64_t*) (sendline+7)) = (uint64_t) htonll(25);

    fd = socket(AF_INET, SOCK_STREAM, 0);
    bzero((char*)&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    serv_addr.sin_port = htons(22444);
    connect(fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr));
    len = (int) send(fd, sendline, 15, 0);
    len = (int) recv(fd, recvline, 4, 0);
    printf("Received %d bytes: %d\n", len, ntohl(*((uint32_t*)recvline)));
    close(fd);
}