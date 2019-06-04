#include <iostream>
#include "Wallet.h"

int silnia (int tmp) {
	int silniaRes;
	if ( tmp == 0 ) {
		silniaRes = 1;
	}
	else {
		int arg = tmp - 1;
		silniaRes = silnia ( arg ) * tmp;
	}
	return silniaRes;
}


int main( ) {
	int res = silnia ( 0 );
	int resu = silnia ( 4 );
	std::cout << res << std::endl;
	std::cout << resu << std::endl;
	return 0;
}