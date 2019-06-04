#ifndef WALLET_H
#define WALLET_H
#include <string>
#include "CurrenciesLibrary.h"

class Wallet {
	float amount;
	std::string currencyID;

public:
	Wallet(std::string currencyID, float amount) {
		this->amount = amount;
		this->currencyID = currencyID;
	}

	Wallet(std::string currencyID, Wallet object) {
		this->currencyID = currencyID;
		this->amount = object.conversion(currencyID);
	}

	Wallet(std::string currencyID) {
		this->amount = 0;
		this->currencyID = currencyID;
	}

	float getAmount() {
		return amount;
	}

	float getAmount() const {
		return amount;
	}

	std::string getID() {
		return currencyID;
	}

	std::string getID() const {
		return currencyID;
	}

	void setAmount(float amount) {
		this->amount = amount;
	}

	float conversion(std::string currencyID) {
		return amount * CurrenciesLibrary::getInstance().calculateCourse(this->currencyID, currencyID );
	}

	float conversion(std::string currencyID) const {
		return amount * CurrenciesLibrary::getInstance().calculateCourse(this->currencyID, currencyID );
	}

	//operator == for all data types
	friend bool operator==(const Wallet& first, const Wallet& second) {
		float firstAmount = first.conversion(second.currencyID);
		return (firstAmount == second.amount);
	}

	friend bool operator==(const Wallet& first, const int& second) {
		return (first.amount == second);
	}

	friend bool operator==(const Wallet& first, const float& second) {
		return (first.amount == second);
	}

	friend bool operator==(const int& first, const Wallet& second) {
		return (first == second.amount);
	}

	friend bool operator==(const float& first, const Wallet& second) {
		return (first == second.amount);
	}

	//operator > for all data types
	friend bool operator>(const Wallet& first, const Wallet& second) {
		float firstAmount = first.conversion(second.currencyID);
		return (firstAmount > second.amount);
	}

	friend bool operator>(const Wallet& first, const int& second) {
		return (first.amount > second);
	}

	friend bool operator>(const Wallet& first, const float& second) {
		return (first.amount > second);
	}

	friend bool operator>(const int& first, const Wallet& second) {
		return (first > second.amount);
	}

	friend bool operator>(const float& first, const Wallet& second) {
		return (first > second.amount);
	}

	//operator < for all data types
	friend bool operator<(const Wallet& first, const Wallet& second) {
		float firstAmount = first.conversion(second.currencyID);
		return (firstAmount < second.amount);
	}

	friend bool operator<(const Wallet& first, const int& second) {
		return (first.amount < second);
	}

	friend bool operator<(const Wallet& first, const float& second) {
		return (first.amount < second);
	}

	friend bool operator<(const int& first, const Wallet& second) {
		return (first < second.amount);
	}

	friend bool operator<(const float& first, const Wallet& second) {
		return (first < second.amount);
	}

	//operator != for all data types
	friend bool operator!=(const Wallet& first, const Wallet& second) {
		return !(first == second);
	}

	friend bool operator!=(const Wallet& first, const int& second) {
		return !(first == second);
	}

	friend bool operator!=(const Wallet& first, const float& second) {
		return !(first == second);
	}

	friend bool operator!=(const int& first, const Wallet& second) {
		return !(first == second);
	}

	friend bool operator!=(const float& first, const Wallet& second) {
		return !(first == second);
	}

	//operator >= for all data types
	friend bool operator>=(const Wallet& first, const Wallet& second) {
		return (first == second || first > second);
	}

	friend bool operator>=(const Wallet& first, const int& second) {
		return (first == second || first > second);
	}

	friend bool operator>=(const Wallet& first, const float& second) {
		return (first == second || first > second);
	}

	friend bool operator>=(const int& first, const Wallet& second) {
		return (first == second || first > second);
	}

	friend bool operator>=(const float& first, const Wallet& second) {
		return (first == second || first > second);
	}

	//operator <= for all data types
	friend bool operator<=(const Wallet& first, const Wallet& second) {
		return (first == second || first < second);
	}

	friend bool operator<=(const Wallet& first, const int& second) {
		return (first == second || first < second);
	}

	friend bool operator<=(const Wallet& first, float& second) {
		return (first == second || first < second);
	}

	friend bool operator<=(const int& first, const Wallet& second) {
		return (first == second || first < second);
	}

	friend bool operator<=(const float& first, Wallet& second) {
		return (first == second || first < second);
	}

	//operator + for all data types
	friend Wallet operator+(Wallet left, const Wallet& right) {
		left.amount = left.amount + right.conversion(left.currencyID);
		return left;
	}

	friend Wallet operator+(Wallet left,  const int& right) {
		left.amount = left.amount + right;
		return left;
	}

	friend Wallet operator+(Wallet left, const float& right) {
		left.amount = left.amount + right;
		return left;
	}

	friend Wallet operator+(int left, const Wallet& right) {
		return Wallet(right.currencyID, left + right.amount);
	}

	friend Wallet operator+(float left, const Wallet& right) {
		return Wallet(right.currencyID, left + right.amount);
	}

	//operator - for all data types
	friend Wallet operator-(Wallet left, const Wallet& right) {
		left.amount = left.amount - right.conversion(left.currencyID);
		return left;
	}

	friend Wallet operator-(Wallet left, const int& right) {
		left.amount = left.amount - right;
		return left;
	}

	friend Wallet operator-(Wallet left, const float& right) {
		left.amount = left.amount - right;
		return left;
	}

	friend Wallet operator-(int left, const Wallet& right) {
		return Wallet(right.currencyID, left - right.amount);
	}

	friend Wallet operator-(float left, const Wallet& right) {
		return Wallet(right.currencyID, left - right.amount);
	}

	//operator * for all data types
	friend Wallet operator*(Wallet left, Wallet& right) {
        return Wallet(left.currencyID, left.amount*right.amount);
	}

	friend Wallet operator*(Wallet left, int& right) {
		left.amount = left.amount * right;
		return left;
	}

	friend Wallet operator*(Wallet left, const float& right) {
		left.amount = left.amount * right;
		return left;
	}

	friend Wallet operator*(int left, Wallet& right) {
		return Wallet(right.currencyID, left * right.amount);
	}

	friend Wallet operator*(float left, const Wallet& right) {
		return Wallet(right.currencyID, left * right.amount);
	}

	//operator / for all data types
	friend Wallet operator/(Wallet left, Wallet& right) {
        return Wallet(left.currencyID, left.amount/right.amount);
	}

	friend Wallet operator/(Wallet left, int& right) {
		left.amount = left.amount / right;
		return left;
	}

	friend Wallet operator/(Wallet left, const float& right) {
		left.amount = left.amount / right;
		return left;
	}

	friend Wallet operator/(int left, Wallet& right) {
		return Wallet(right.currencyID, left / right.amount);
	}

	friend Wallet operator/(float left, const Wallet& right) {
		return Wallet(right.currencyID, left / right.amount);
	}

	//operator = for all data types
	Wallet& operator =(const Wallet right) {
		this->amount = right.conversion(this->currencyID);
		return *this;
	}

	Wallet& operator =(int right) {
		this->amount = right;
		return *this;
	}

	Wallet& operator =(float right) {
		this->amount = right;
		return *this;
	}

	operator int() {
		return amount;
	}

	operator float() {
		return amount;
	}
};

//operator ostream <<
std::ostream& operator<<(std::ostream& os, const Wallet& obj) {
	os << obj.getAmount() << obj.getID();
	return os;
}

//operator istream >>
std::istream& operator>>(std::istream& is, Wallet& obj) {
	float tmp;
	is >> tmp;
	obj.setAmount(tmp);
	return is;
}

#endif
