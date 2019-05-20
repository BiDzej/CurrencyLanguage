#ifndef CURRENCIES_LIBRARY_H
#define CURRENCIES_LIBRARY_H
#include <map>

class CurrenciesLibrary {
	std::map<std::string, float> currencies;
	
	//here we have to add all known currencies
	CurrenciesLibrary() {
		currencies["PLN"] = 1.0f;
		currencies["EUR"] = 0.232143625f;
		currencies["CHF"] = 0.26205793f;
		currencies["USD"] = 0.259245f;
		currencies["GBP"] = 0.203841814f;
		currencies["JPY"] = 28.5354981f;
		currencies["RUB"] = 16.7872175f;
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
		currencies[idNew] = currencies[idComp] / course;
	}

	//calculate course one to other currency
	float calculateCourse(std::string first, std::string second) {
		return currencies[second] / currencies[first];
	}

};

#endif
