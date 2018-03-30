# Integrador 2018 Automation API Tests

To execute the test you must follow these steps:

 - Clone the repository
 - If you don't have protractor already installed, or selenium webdriver-manager, execute in a terminal:
	- npm install -g protractor
	- webdriver-manager update
 - In a new terminal (you should use 2 terminals at the same tame) execute:
 	- webdriver-manager start
 - Go to this this project's folder in the other terminal
 - Execute these commands: 
	 - npm install
	 - protractor conf.js
 - The test will run
 - As the API is stored in a free heroku server, it must be not running at the first attempt to run the tests and they could fail, in that case try a second time.

Who made which test:
 - EP-326: John Bryan Yepez Herrera

**By : Universidad Nacional Team**
