//package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	static public void main(String[] args) {
		Scanner scnr = new Scanner(System.in);

		Socket sock = null;
		BufferedReader	input = null;
		PrintWriter output = null;
		
		final String address = "127.0.0.1";
		final int port = 80;
		
		try {
			sock = new Socket(address, port);
			System.out.print("Connected\n\nWrite your message:\n");
			
			output = new PrintWriter(sock.getOutputStream(), true);

			input = new BufferedReader(
				new InputStreamReader(sock.getInputStream()));

		} catch (UnknownHostException e) {
			System.out.println(e);
			return;
			
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
		
		String buff = "";
		
		while(!buff.equals("quit")) {
			try {
				System.out.print("> ");
				buff = scnr.nextLine();
				output.println(buff);
				//essentially this just keeps reading lines until readLine() returns null
				while((buff += input.readLine() ) != null);
				System.out.printf("[SERVER]: %s\n", buff);

			} catch(IOException e) {
				System.out.println(e);
			}
		}
		System.out.printf("Connection Closed\n\nLine: \"%s\"", buff);

		
		try {
			output.close();
			scnr.close();
			sock.close();
		} catch(IOException e) {
			System.out.println(e);
		}
		
	}
}
