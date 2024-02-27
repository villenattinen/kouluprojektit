#include <math.h>

double kartion_tilavuus(double sade, double korkeus);

double kartion_tilavuus(double sade, double korkeus) {
	return (M_PI * sade * sade * korkeus) / 3;
}