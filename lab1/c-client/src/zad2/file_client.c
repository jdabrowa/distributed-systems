#include <stdio.h>
#include <unistd.h>
#include "file_client.h"
#include "net.h"

#define SUCCESSFUL_REQUEST 200
#define NO_SUCH_IMAGE -1

char *localhost = "127.0.0.1";
int port_no = 25434;

int main(int argc, char *argv[]) {

    char line_buff[100];
    char resp_buff[1024];
    char msg_len_buf[4];
    int msg_len;

    int socket_fd = create_and_connect_socket(localhost, port_no);
    ssize_t stale = recv(socket_fd, resp_buff, 1000, MSG_DONTWAIT);
    printf("%d bytes of stale data received\n", stale);

    printf("Enter file name (available are 'moon', 'lena', 'lambo', no quotes:\n");
    scanf("%s", line_buff);
    msg_len = (int) strlen(line_buff);
    printf("Sending request for %s of length %d...\n", line_buff, msg_len);
    *((uint32_t *)msg_len_buf) = (uint32_t) htonl((u_int32_t) msg_len);
    send(socket_fd, (void const *) msg_len_buf, 4, 0);
    send(socket_fd, line_buff, strlen(line_buff), 0);

    int request_status = read_int(socket_fd);
    int name_len;

    switch (request_status) {
        case SUCCESSFUL_REQUEST:
            name_len = read_int(socket_fd);
            printf("Name length: %d\n", name_len);
            int count = 0;
            while(count < name_len) {
                int c = recv(socket_fd, resp_buff+count, name_len-count, 0);
                count += c;
            }
            resp_buff[name_len] = 0;
            printf("Request successful, receiving image '%s'...\n", resp_buff);
            int num_bytes = read_int(socket_fd);
            printf("Expecting %d bytes...\n", num_bytes);
            FILE *dest_file = fopen(resp_buff, "w");

            count = 0;
            while (count < num_bytes) {
                int received = recv(socket_fd, resp_buff, 1024, 0);
                count += received;
                fwrite(resp_buff, 1, received, dest_file);
            }
            fclose(dest_file);

            break;
        case NO_SUCH_IMAGE:
            printf("Received response from server: request failed, no such image\n");
            break;
        default:
            printf("Received unknown error code: %d\n", request_status);
    }

    sleep(1);

    return 0;
}

uint32_t read_int(int socket_fd) {
    char resp_buff[4];
    int counter = 0;
    while(counter < 4) {
        int len = (int) recv(socket_fd, resp_buff+counter, 4-counter, 0);
        counter += len;
        printf("Read_int returned %d bytes\n", len);
    }
    return ntohl(*((uint32_t*)resp_buff));
}