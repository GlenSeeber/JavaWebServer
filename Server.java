//package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		//variables
		final int 		PORT = 80;
		
		Socket			sock = null;
		ServerSocket 	server = null;
		BufferedReader	input = null;
		PrintWriter output = null;
		
		String inBuff = "";
		String outBuff = "";
		
		try {
			//create Socket
			server = new ServerSocket(PORT);
			System.out.println("Server Started\n\nWaiting for clients...");
			
			//accept client (TODO: move this into a loop)
			while (true){
				sock = server.accept();
				System.out.println("Client accepted");
				
				//setup input and output pipelines
				input = new BufferedReader(
					new	InputStreamReader(sock.getInputStream()));
	
				output = new PrintWriter(sock.getOutputStream(), true);
				
				//create empty string for storing most recent input
				
				//keep talking until they send "Over"
				//while (!buff.equals("quit")) {
				try {
					//replace buff with the most recent input transmission
					inBuff = input.readLine();
					//display the transmission
					System.out.printf("[CLIENT]: %s\n", inBuff);

					String filename = "";
					if (inBuff.startsWith("GET")){
						Scanner fscnr = null;
						try{
							//make a substring of just "/[resource]", and ending at the newline
							try{
								filename = inBuff.substring(5, inBuff.indexOf(" ", 5));
							} catch (Exception e){
								System.out.println(e);
								filename = inBuff.substring(5);
							}
							//try to open and read the requested resource
							System.out.println(filename);
							File resource = new File(filename);
							fscnr = new Scanner(resource);
							//write it to a byte array
							//outBuff = "200 OK\n";
							while (fscnr.hasNextLine()){
								outBuff += fscnr.nextLine();

							}
							System.out.println(outBuff);
							output.println(outBuff);
							output.println(inBuff);
						} catch(FileNotFoundException e){
							System.out.println(e);
							output.print("404 Error");
							System.out.println("File couldn't be found, sending error to client");
						} catch(Exception e){
							System.out.println(e);
						} finally{
							if(fscnr != null){
								fscnr.close();
							}
						}
					}

				} catch (IOException e) {
					System.out.println(e);
				}
					//}
				System.out.println("\n\nwaiting for new clients...");
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	}
	
	
	
}
