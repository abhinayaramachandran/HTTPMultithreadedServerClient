
/*
 * Author : Abhinaya Ramachandran
 * 
 *
 * Description : The WebServer class acts as an entry point
 * for the multi-threaded web server.
 * 
 * Inputs : Port number 
 * If this input is not provided a default port of 8080 is chosen 
 * 
 * 
 * References: http://cs.lmu.edu/~ray/notes/javanetexamples/
 * http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm
 * https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
 **/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class WebServer {
	static ServerSocket serverSocket=null;
	static int portNumber = 0;
	
	public static void main(String args[]){
		// Checking if user provides a valid port.
		// Port 8080 is used if no port is provided.
		if (args.length == 1){
			try {
				portNumber = Integer.parseInt(args[0]);
			}catch(Exception e){
				System.out.println("<SERVER>: Error ! Found a non integer Port Number.");
			}
		}else{
			 portNumber = 6000;
		}
		
		// Open a socket at the port assigned
		try {
		      serverSocket = new ServerSocket(portNumber);
		      System.out.println("<SERVER>: Serving on port " +portNumber);
		    } catch (IOException e) {
		      System.out.println(e);
		    }
		
		
		while (true) {
			try {
				//Accepting the incoming connection and delegating it to
				// RequestHandler
				Socket s =serverSocket.accept();	
				System.out.println("<SERVER>: Accepted Client Connection. Initiating request handling.");
				RequestHandler request = new RequestHandler( s );
				
				// The request is processed by a new thread, so that the server
				// can continue receiving more requests on the main thread
				Thread thread = new Thread(request);
				System.out.println("<SERVER>: Starting new thread with id " + thread.getId());
				thread.start();
				
			}catch (Exception e) {
				e.printStackTrace();
			}	
			

		}
		
	}
}