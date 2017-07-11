/*
 * Author : Abhinaya Ramachandran
 * 
 * 
 * Description : The WebClient class sends request to the client on the 
 * specified port.
 * WebClient also performs logging of the requests and responses.
 * 
 * Inputs : The port number and the file to be requested
 * If the port number is not provided, the default port used is 8080
 * If the port number and the filename are not provided, 
 * then some default filenames are used
 * on port 8080
 * */


import java.net.*;
import java.io.*;

public class WebClient {
	
	static final String CRLF="\r\n";
	
	public static void main(String[] args){
	  /*
	   * For command line input
	   * Checking if the socket number and file name are valid
	   * */
	  int portNumber = 0;
	  String host="";
	  boolean userFile = true;
	  String requestedFile ="";
	 
	  if (args.length >= 2){
		  // Port number is provided
			try {
				portNumber = Integer.parseInt(args[1]);
				host = args[0];
			}catch(Exception e){
				System.out.println("<CLIENT>: Error ! Found a non integer Port Number.");
			}
		}else{
			 portNumber = 8080;
			 host = "127.0.0.1";
		}
	  
	  // Checking if file name is provided by user
	  if(args.length <= 2){
		  // Filename is not provided
		  userFile = false;
	  }else if (args.length == 3 ){
		  // Filename is provided by the user
		  requestedFile = args[2];
	  }
		
    try{
      // Creating a doorway to the Server
      Socket s = new Socket( host, portNumber);
      System.out.println("<CLIENT>: Connected to Server Successfully");
      
      // Generating the request
      PrintStream ps =new PrintStream(s.getOutputStream());
      
      if (!userFile){
    	  // If the user has not provided the file some default filenames are used 
    	  // as input
    	  String request = "GET /index2.html HTTP/1.1"+CRLF+CRLF;
	      System.out.println("<CLIENT>: Sending Request "+request);
	      ps.println(request);
	      ps.flush();
      }else{
    	  
    	  // If the user has provided the file
    	  String request = "GET "+requestedFile+" HTTP/1.1"+CRLF+CRLF;
    	  System.out.println("<CLIENT>: Sending Request "+ requestedFile );
          ps.println(request);
          ps.flush();
      }
      
      System.out.println("<CLIENT>: Receiving the file");
      InputStream is = s.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String res = br.readLine();
      while (res != null && res.length() != 0){
    	  System.out.println(res);
    	  res = br.readLine();
      }
      
      System.out.println("<CLIENT>: Successfully received the file");
      
      System.out.println("<CLIENT>: Closing connection with the server");
     
      // Closing sockets, input and output streams 
      is.close();
      br.close();
      ps.close();
      s.close();
      
    }catch(IOException ioe){
      System.out.println("<CLIENT>: There has been an error.");
      ioe.printStackTrace();
    }
  }
}