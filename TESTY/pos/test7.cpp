#include <iostream>
#include "Wallet.h"

void menu () {
	std::cout << "Choose your option:" << std::endl;
	std::cout << " 1. EUR => PLN" << std::endl;
	std::cout << " 2. PLN => EUR" << std::endl;
	std::cout << " 3. PLN => USD" << std::endl;
	std::cout << " 4. USD => PLN" << std::endl;
	std::cout << " 5. EUR => USD" << std::endl;
	std::cout << " 6. USD => EUR" << std::endl;
	std::cout << " 0. close the program" << std::endl;
}


int main( ) {
	int option;
	menu ( );
	std::cin >> option ;
	while( option != 0 ) {
		if ( ( option > 0 && option < 7 ) ) {
			std::cout << "write amount of money: " << std::endl;
			float amount;
			std::cin >> amount ;
			if ( option == 1 ) {
				Wallet tmp("EUR", amount);
				Wallet res("PLN", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
			else if( option == 2 ) {
				Wallet tmp("PLN", amount);
				Wallet res("EUR", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
			else if( option == 3 ) {
				Wallet tmp("PLN", amount);
				Wallet res("USD", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
			else if( option == 4 ) {
				Wallet tmp("USD", amount);
				Wallet res("PLN", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
			else if( option == 5 ) {
				Wallet tmp("EUR", amount);
				Wallet res("USD", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
			else {
				Wallet tmp("USD", amount);
				Wallet res("EUR", tmp);
				std::cout << "result: " << std::endl;
				std::cout << res << std::endl;
			}
		}
		else {
			std::cout << "Incorrect option number." << std::endl;
		}
		std::cout << "\n" << std::endl;
		menu ( );
		std::cin >> option ;
	}
	return 0;
}