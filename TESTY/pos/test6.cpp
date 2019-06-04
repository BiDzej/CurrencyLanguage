#include <iostream>
#include "Wallet.h"

int main( ) {
	Wallet eur("EUR", 10);
	Wallet usd("USD", 51.5f );
	Wallet ticket("PLN", 3.2f );
	if ( ( eur > 0 && 30 * ticket <= eur ) || ( usd > 0 && 30 * ticket <= usd ) ) {
		std::cout << "we can pay in one currency" << std::endl;
	}
	else if( eur + usd > 30 * ticket ) {
		std::cout << "we can pay using both currencies" << std::endl;
	}
	else {
		std::cout << "no enough money" << std::endl;
	}
	return 0;
}