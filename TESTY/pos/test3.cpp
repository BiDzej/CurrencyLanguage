#include <iostream>
#include "Wallet.h"

int main( ) {
	Wallet wal("EUR", 10);
	Wallet wall("EUR", 10);
	Wallet ticket("USD", 1.5f );
	Wallet res("PLN", wal - ( 3 * ticket ));
	Wallet resu("PLN", wall);
	for ( int i= 1 ; i <= 3; ++i ) { 
		resu = resu - ticket;
	}
	std::cout << res << std::endl;
	std::cout << resu << std::endl;
	return 0;
}