-- Database name budgetlogin

CREATE TABLE useraccount (
	iduseraccount SERIAL PRIMARY KEY,
	firstname VARCHAR(45) NOT NULL,
	lastname VARCHAR(45) NOT NULL,
	username VARCHAR(45) UNIQUE NOT NULL,
	password VARCHAR(45) NOT NULL
);

CREATE TABLE account (
    accountid SERIAL PRIMARY KEY,
    accountname VARCHAR(45) NOT NULL,
    userid INT NOT NULL,
    active boolean NOT NULL,
    FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount)
);

CREATE TABLE category (
	categoryid SERIAL PRIMARY KEY,
	categoryname VARCHAR(15) NOT NULL,
	userid INT NOT NULL,
	FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount)
);

CREATE TABLE budget (
	budgetid SERIAL PRIMARY KEY,
	categoryid INT UNIQUE NOT NULL,
	amount DECIMAL(11,2) NOT NULL,
	date DATE NOT NULL,
	userid INT NOT NULL,
	FOREIGN KEY (categoryid) REFERENCES category(categoryid),
	FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount)
);

CREATE TABLE transaction(
	transactionid SERIAL PRIMARY KEY,
	userid INT NOT NULL,
	date DATE NOT NULL,
	accountid INT NOT NULL,
	categoryid INT NOT NULL,
	amount DECIMAL(11,2) NOT NULL,
	transactionname TEXT NOT NULL,
	FOREIGN KEY (categoryid) REFERENCES category(categoryid),
	FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount),
	FOREIGN KEY (accountid) REFERENCES account(accountid)
);

CREATE TABLE recurringexpense(
	recurid SERIAL PRIMARY KEY,
	categoryid INT NOT NULL,
	startdate DATE NOT NULL,
	enddate DATE,
	amount DECIMAL(11,2) NOT NULL,
	permonth BOOLEAN NOT NULL,
	peryear BOOLEAN NOT NULL,
	userid INT NOT NULL,
	accountid INT NOT NULL,
	active BOOLEAN NOT NULL,
	transactionname TEXT UNIQUE NOT NULL,
	FOREIGN KEY (categoryid) REFERENCES category(categoryid),
	FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount),
	FOREIGN KEY (accountid) REFERENCES account(accountid)
);

CREATE TABLE savingsgoal(
	goalid SERIAL PRIMARY KEY,
	savedupval DECIMAL(11,2) NOT NULL,
	goalval DECIMAL(11,2) NOT NULL,
	userid INT UNIQUE NOT NULL,
	accountid INT UNIQUE NOT NULL,
	FOREIGN KEY (userid) REFERENCES useraccount(iduseraccount),
	FOREIGN KEY (accountid) REFERENCES account(accountid)
);