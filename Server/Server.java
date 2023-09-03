package Server;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) {
		final int 		PORT = 80;

		ServerUtil utility = new ServerUtil();

		try{
			//create Socket
			utility.server = new ServerSocket(PORT);
			System.out.println("Server Started!\n");

		} catch(IOException e){
			System.out.printf("Error setting up listening socket: %s", e);
		}
		while(true){
			System.out.println("Wating for Clients...");
			try{
				//accept client
				utility.sock = utility.server.accept();
				System.out.println("Client accepted");
				//deal with the client
				utility.handleClient();
			} catch(IOException e){
				System.out.printf("Socket Error accepting and/or handling client: %s", e);
			}
		}
	}

}