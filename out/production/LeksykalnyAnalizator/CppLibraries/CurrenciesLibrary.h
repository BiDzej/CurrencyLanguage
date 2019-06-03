#ifndef CURRENCIES_LIBRARY_H
#define CURRENCIES_LIBRARY_H
#include <map>

class CurrenciesLibrary {
	std::map<std::string, float> currencies;

	//here we have to add all known currencies
	//CURRENCY : PLN
	CurrenciesLibrary() {
		currencies["PLN"] = 1.0f;
		currencies["EUR"] = 4.2786f;
		currencies["CHF"] = 3.8294f;
		currencies["USD"] = 3.8028f;
		currencies["GBP"] = 4.8163f;
		currencies["JPY"] = 0.0351f;
		currencies["RUB"] = 0.0582f;
	}

public:
	//destructor
	~CurrenciesLibrary() {}

	static CurrenciesLibrary& getInstance() {
		static CurrenciesLibrary instance;
		return instance;
	}

	//get course of currency
	float getCourse(std::string id) {
		std::map<std::string, float>::iterator it = currencies.find(id);
		if (it == currencies.end())
			return -1;
		return it->second;
	}

	//set course of any currency to pln
	void setCourse(std::string id, float course) {
		currencies[id] = course;
	}

	//set course NEW : ANY EXISTING
	void setCourse(std::string idNew, std::string idComp, float course) {
		currencies[idNew] = course * currencies[idComp];
	}

	//calculate course one to other currency
	float calculateCourse(std::string first, std::string second) {
		return currencies[first] / currencies[second];
	}

};

#endif
