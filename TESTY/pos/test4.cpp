#include <iostream>
#include "Wallet.h"

int main( ) {
	float n = -10;
	int counter = 0;
	while( n < 0 ) {
		counter = counter + 1;
		n = n + 1.5f ;
	}
	std::cout << counter << std::endl;
	std::cout << n << std::endl;
	return 0;
}