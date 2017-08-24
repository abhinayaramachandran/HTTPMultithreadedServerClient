#HTTPMUltithreadedServerClient
~~~
Implementation Details:
	• Development language: Java
	• IDE used: Eclipse Kepler
	• Java Version: JDK 1.8.0
	• Browser used for testing: Mozilla Firefox, Google Chrome
Web Server:
The Web Server is designed to accept requests from multiple clients. This is enabled by implementing the Runnable interface and overriding its run() method. The web server creates a new thread for every client that issues a request. The client could have a user agent (browser) or send requests from a terminal. When a client request is received, a TCP connection is made to service the request. The web server is implemented by the two classes
	• WebServer.java:
		o Contains the main thread that creates other threads to handle client requests.
		o Obtains the port number from the client if available, otherwise uses the port 8080 to serve requests.
		o Logs server connection status.
	• RequestHandler.java:
		o Implements java’s Runnable interface, contains the code that each thread should implement.
		o Validates and filters the request to fetch the appropriate file.
		o Sends the HTTP status message as well as the payload (data) to the client.
		o Logs all the request handling actions of the server.
		o The can respond with three different status lines:
▪ “HTTP/1.1 200 OK”: When the request is valid and the file is found.
▪ “HTTP/1.1 400 Bad Request”: When the request is invalid or incomprehensible.
▪ “HTTP/1.1 404 Not Found”: When the requested file is absent.

The server can be started from the terminal using the following command
➢ javac WebServer.java RequestHandler.java
➢ java WebServer <port number>


Web Client:
The Web Client sends HTTP requests to the server. If the input port is provided by the user, the given port is used, otherwise port 8080 is used.
The web client is implemented by
	• WebClient.java:
		o Uses a user input or default port to contact server.
		o Uses user given input filename or a predefined filename as requested file name
		o Reads the server response from the socket and displays it either in the browser or in the terminal
		o Logs client events.
	The client can be run from the terminal using the following command:
	➢ javac WebClient.java
	➢ java WebClient <host name/IP address> <port number> <path to file>



~~~

Code References:
http://cs.lmu.edu/~ray/notes/javanetexamples/
http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm
https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
http://crystal.uta.edu/~datta/teaching/cse4344_Summer2017/Programming%20Assignment%201_reference_Java.pdf
