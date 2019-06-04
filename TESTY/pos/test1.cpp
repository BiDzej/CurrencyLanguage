#include <iostream>
#include "Wallet.h"

int main( ) {
	CurrenciesLibrary::getInstance().setCourse("ABC", "USD", 2);
	Wallet pocketL("ABC", 10 * ( 1 + 1 ));
	Wallet pocketR("ABC", ( 5 + pocketL - 100 / 10 ));
	Wallet resultPLN("PLN", pocketL + pocketR);
	Wallet resultUSD("USD", pocketL - pocketL);
	std::cout << pocketL << std::endl;
	std::cout << pocketR << std::endl;
	std::cout << resultPLN << std::endl;
	std::cout << resultUSD << std::endl;
	return 0;
}