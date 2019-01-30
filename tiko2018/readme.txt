Coursework for the databaseprogramming course of 2018.

The code imitates a antiquarian shops database with one base antiquarian shop, one branch shop and their customer information.
It is command line based, stable and could be applied to any store with branch stores with some minor changes.

The database itself is made in PostgreSQL and its schema in finnish is in the "schema" -file

Brief translation of the documentation file:

The program is split in to eight classes which function like a waterfall. Higher class calls the lower ones and so on. Eight classees are split into one running class (Tiko), two classes which handle transitions (Kayttoliittyma (=user interface), and User). There is also one class with helping operations (Help) and the rest of the classes handle commands executed by the user.

Kayttoliittyma-class includes the login process, and after a succesfull login (or new user creation) the user will communicate with the User-class. Activating the system used to communicate with databases is activated from this class.

Commands: (A before the number indicates that the command is for admin use only)
1.	help
2.	newCart
3.	search
4.	add
5.	remove
6.	cart
7.	cashout
8.	discard
9.	exit
A10.	query
A11.	addItem
A12.	promote
A13.	demote
A14.	reset
A15.	triggerTest
A16.	r2

The main functionality of the coursework is to be able to shop books from an antiquarian shop. After creating an user into the site, te user is able to search the database for books he/she is interested in. The books can be added to the shopping cart which is not removed if the user logs out. The command cashout has no actual cashingout built into it, it just assumes a third-party program will handle the payment.
Admincommands are aimed towards a sysadmin who is doing the upkeeping. Query allows the admin to execute SQL-queries of any type in to the database, promoting and demoting are for upgrading an user to admin status, reset resets the tables to the "current state" of the database. TriggerTest and r2 were commands to get more points on the coursework.

