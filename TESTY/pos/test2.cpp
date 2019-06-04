#include <iostream>
#include "Wallet.h"

int main( ) {
	CurrenciesLibrary::getInstance().setCourse("ABC", "EUR", 2.0);
	Wallet abc("ABC", 1.2f );
	Wallet eur("EUR", 2.4f );
	if ( abc < eur ) {
		std::cout << "Smaller" << std::endl;
	}
	else if( abc > eur ) {
		std::cout << "Greater" << std::endl;
	}
	else {
		std::cout << "Equal" << std::endl;
	}
	return 0;
}