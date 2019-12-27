## Auto-tests for login using MySQL.
### How to start:

1. clone https://github.com/vadikpkin/login-test-sql/
2. start docker-compose container with a docker-compose file from "docker" folder in repository(That will run container with MySQL with prepared database on your 3306 port by deafault.)
3. run artifacts/app-deadline.jar on you local host.
4. open terminal and and type "./gradlew test" or "./gradlew.bat test," for Windows. You need to restart app every time after tests executes or delete @AfterAll method. Application puts users in database every time it executes, so you need to keep your users table clean.
 
