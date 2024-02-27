#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

float x = 976.672;
short int y = 14;
long int z = 6563653;

int main() {

sprintf(str, "%027.3f, %027.3f, %027.3f", x, y, z);

return 0;
}
