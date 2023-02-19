# java_budgetapp

### Preface
This Budgeting app is built with Java and JavaFX. The goal of this app was to make a simple app to basically be a budget planner. It tracks transactions and lets you know how much over/under you are according to your overall monthly budget and monthly budget for each budget category. This app also allows for multiple accounts to use the app with their own independent budgets via a login/signup system. 

### How to use
There is a couple things you need to do before you can get this app up and running on your end. Mainly to do with the database. 

First thing you want to do is set up the database. I provided a sql schema file in this github repo named "budgetlogin.sql", which should provide the instructions to make your sql database work with this. 

The second thing you want to do is connect your database to the code, this should all be done in the "DatabaseConnection" class under HelperClasses. That class is currently set up with a postgresql database driver, so if you are using a different database its best to change the driver. Other than changing the driver the only thing left to do is to connect via credentials and database URL/port in lines 12-15 in the DatabaseConnection class file.

If you want to convert this into a executable file or JAR after you are done configuring the database and setting it up in the code I would recommend following this guide: https://medium.com/@vinayprabhu19/creating-executable-javafx-application-part-2-c98cfa65801e 

Other than that, enjoy the Pocket Budgeting App :)
