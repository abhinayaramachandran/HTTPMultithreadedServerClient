/*
 * Author : Abhinaya Ramachandran
 * 
 *
 * Description : The RequestHandler class reads the
 * request from the client and fetches the desired file,
 * it then sends the file to the client over the socket.
 **/


import java.io.*;
import java.net.*;
import java.util.*;

/* 
 * The RequestHandler class implements the Runnable interface 
* to create multiple threads on which the Web Server listens 
* for requests.
* */

final class RequestHandler implements Runnable
{
	static Socket socket;
	static final String CRLF = "\r\n";
	boolean agent = false;
	public RequestHandler(Socket soc) throws Exception 
	{
		socket = soc;
	}

	// The run method of Runnable Interface has to be overridden
	public void run()
	{
		try {
			System.out.println("<SERVER>: Processing input from the client.");
			prepareResponse();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void prepareResponse() throws Exception
	{	
		// To read and write data from and to the socket
		InputStream in = socket.getInputStream();
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		/*
		 * HTTP response message has
		 * Status Line
		 * CRLF
		 * CRLF
		 * Message Body
		 * */
		String statusLine = null;
		String messageBody = null;
		
		// File Related
		FileInputStream fi = null;
		boolean found = true;
		boolean badRequest = false;
		
		// Read the first line 
		String requestLine = br.readLine();
		System.out.println("---------------------------------------------------------------------");
		System.out.println("<SERVER>: The request received from thread with id "+Thread.currentThread().getId()+ "  is  "+requestLine);
		System.out.println("---------------------------------------------------------------------");
		if (requestLine != null){
		// Displays attributes of the connection
		String line = br.readLine();		
		while (line != null && line.length() != 0) {
			agent = true;
			System.out.println(line);
			line = br.readLine();
		}
		System.out.println("---------------------------------------------------------------------");
		// Splits the request and extracts file name 
		StringTokenizer st = new StringTokenizer(requestLine);
		String requestType =st.nextToken();
		if(!requestType.contains("GET")){
			System.out.println("request"+requestType);
			badRequest = true;
		}  
		String file = st.nextToken();
		// Default page with no files requested
		if (file.endsWith("/") || file == null){
			file ="/index2.html";
		}
		file = "." + file;
		System.out.println("<SERVER>: Fetching File "+file);
		
		String contentType = "";
		try {
			fi = new FileInputStream(file);
			if(file.endsWith(".htm") || file.endsWith(".html")) {
				contentType= "text/html";
			}
			else if(file.endsWith(".jpg") || file.endsWith(".jpeg")) {
				contentType= "image/jpeg";
			}
			else if(file.endsWith(".png")) {
				contentType= "image/png";
			}
			else if(file.endsWith(".gif")) {
				contentType= "image/gif";
			}else if(file.endsWith(".class")){
				contentType= "application/octet-stream";
			}else{
				contentType= "text/plain";
				}
			System.out.println("<SERVER>: Content type of "+file+" is "+contentType);
		} catch (FileNotFoundException e) {
			System.out.println("<SERVER>: File "+file+" was not found");
			found = false;
		}
		
		
		if (badRequest){
			statusLine = "HTTP/1.1 400 Bad Request"+CRLF+CRLF;
			messageBody = "<HTML>" + 
				"<HEAD><TITLE>400 Bad Request !</TITLE></HEAD>" +
				"<BODY><H1>Server did not understand your request !!!</H1>"
				+ "</BODY></HTML>";
		}
		else if (found) {
			statusLine = "HTTP/1.1 200 OK"+CRLF+CRLF;
		} 
		else {
			statusLine = "HTTP/1.1 404 Not Found"+CRLF+CRLF;
			messageBody = "<HTML>" + 
				"<HEAD><TITLE>404 Page Not Found !</TITLE></HEAD>" +
				"<BODY><H1>Oops Page Not Found !!!</H1>You might have requested the wrong file"
				+ "</BODY></HTML>";
		}

		if (found)	{
			// Response is being sent
			// Status Line with CRLF
			if(agent){
				out.write((statusLine).getBytes());
			}
			// Message Body
			transferPayload(fi, out);
			out.flush();
			fi.close();
		} else {
			// Status Line
			out.write((statusLine).getBytes());
			// Message Body
			out.writeBytes(messageBody);
		}
		// Close streams and socket.
		out.close();
		br.close();
		}else{
			System.out.println("<SERVER>: Discarding the null request");
		}
	}
	private static void transferPayload(FileInputStream fi, OutputStream out) 
			throws Exception
			{
			   int n;
			   byte[] fileContents = new byte[4096];
			   // Copy requested file into the socket's output stream.
			   while((n = fi.read(fileContents)) != -1 ) {
			      out.write(fileContents);
			   }
			   out.write((CRLF).getBytes());
			   out.write((CRLF).getBytes());
			System.out.println("<SERVER>: Contents of the file sent to the client with id "+ Thread.currentThread().getId());
			System.out.println("---------------------------------------------------------------------");
			}
}
	