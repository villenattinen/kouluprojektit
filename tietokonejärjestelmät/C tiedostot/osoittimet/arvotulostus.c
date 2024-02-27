#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

void kirjoita_sensorit(char *str, float ax, float ay, float az, float press, float temp);

void kirjoita_sensorit(char *str, float ax, float ay, float az, float press, float temp) {

    sprintf(str, "%+.2f,%+.2f,%+.2f,%d,%.2f", ax, ay, az, (int)(press + 0.5), temp);
}
