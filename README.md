# University ERP Project
This is a complete and comprehensive University ERP System built with Java and keeping in mind security and robustness.

###### By: Pratyaksh Kumar (2024431) and Sourabh (2024563)
## Overview:
The system consists of three views / interfaces, namely `Admin` , `Student` and `Instructor`, for each respective role. Which are all accessible by a single login interface.

The login system is enforced by a `userID` and `passwd` , the user is redirected to their respective interface via the `userType` stored internally in the database, leaving NO CHANCE for any user getting redirected to wrong interface. As the interface opening is a internal decision NOT directly dependent on user inputs.

The password is encrypted and stored via the `password4j` library's `bcrypt2` algorithm, ensuing no plaintext passwords are stored in the database.

## File Structure:
As evident the program is split in packages, here are some notes about the directories / packages:
1. `main/resources` : Contains `db.properties` which contains the `Databse Credentials` and further contains `db/migrations` which itself contains **three sql files** neccessary for setting up the Databse on first run.
2. `main/java/edu/univ/erp/util`: Contains `DatabaseInit` and `DBConnection`, while the first serves as a file to initialize the database, the latter serves as a Connection File for the entire applicatioion.
3. `main/java/edu/univ/erp/ui`: Contains all the UI Interfaces, split into different user types.
4. `main/java/edu/univ/erp/service`: This is primarily where all the backend logic lives, the 4 service files contain all the functins and methods that fetch and put data to / from the database.
5. `main/java/edu/univ/erp/data`: This is where all our data classes live, used in Structuring the data.
6. `main/java/edu/univ/erp/domain`: here lies `User`, which serves as a foundation for managing user state throughout application usage.

## How to Run:
This project was build in IntelliJ IDEA, hence the preffered way to run it is through it. As it handles the maven building by itself

### Through IntelliJ IDEA (Recommended)
1. Ensure you have a `MariaDB` server up and running and set up the `db.properties` file with correct credentials and database URL.
2. Look for `DatabaseInit` in `src/main/java/edu/univ/erp/util/DatabaseInit.java` and run it. This will setup the database structure and fill it with dummy data.
3. Ensure Step 2 Ran Successfully by looking at the output.
4. Head over to `src/main/java/edu/univ/erp/Main.java` and run `Main` 
5. Application will Start



