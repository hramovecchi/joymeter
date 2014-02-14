joymeter-webapi
===============
install maven 3.0 or a newer version
	you can see this tutorial to install maven on windows:
	http://www.mkyong.com/maven/how-to-install-maven-in-windows/
	
Running The Application from the console
	where the pom.xml file is located in the project run:
		mvn clean install. 
	
	If it is success run
		mvn tomcat:run to start the application, it will be listening on http://localhost:8080/joymeter-webapi/webapi/myresource for example
