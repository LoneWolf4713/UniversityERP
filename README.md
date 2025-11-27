# University ERP Project
This is a comprehensive ERP system built with Java, powered by Swing and MariaDB.
Features:
1. Interfaces are defined for Admin, Instructors and Students, ensuring that no one can accesss the interface meant for any other user type.
2. This is enforced by the Bcrypt2 encryption we have implemented, ensuring no plainttext passwords are stored in the DB and hence no user can access any other user's data.
3. Databases are deliberately separated too, the AuthDB stores credentials while the erpDB stores the core academic data.

Tech Stack:
1. Langauge: Java
5. UI Framework: Swing with FlatLaf
6. Database: MariaDB
7. Build Tool: Maven

Running the Application
1. Ensure you have a MariaDB Server running at localhost:3306 with a root user and password: Pass8723  OR adjust the DB Parameters in src/main/resources/db.properties
9. open the project in IntelliJIdea
10. navigate to src/main/java/edu/univ/erp/Main.java and right click and run
11. Project will run

By:
- Pratyaksh Kumar 2024431
- Sourabh Kumar

