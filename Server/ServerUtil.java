package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerUtil {
    //variables
	public final int		MAX_SIZE = 2048;
	
	public ServerSocket		server = null;
	public Socket			sock = null;
	public BufferedReader	input = null;
	public OutputStream 	output = null;
	public FileInputStream fileStream = null;
	
	public String 			inBuff = "";
	public byte[] 			outBuff = new byte[MAX_SIZE];

	public String			resourceFolder = "/home/river/git/JavaWebServer/files";
	

	//Handles the individual client
	public int handleClient(){
		//setup input and output pipelines
		try{
			input = new BufferedReader(new	InputStreamReader(sock.getInputStream()));
			output = new BufferedOutputStream(sock.getOutputStream());
		} catch(IOException e){
			System.out.printf("Socket Error during client setup: %s\n", e);
			return -1;
		}

		//read the input
		try {
			inBuff = input.readLine(); //TODO make this read the whole transmission, not just one line
		}
		catch (IOException e) {
			System.out.println(e);
		}
		//display the transmission
		System.out.printf("[CLIENT]: %s\n", inBuff);


		//GET requests
		if (inBuff.startsWith("GET")){
			String filename = "";
			try{				
				//turn inBuff into the requested resource
				filename = requestResource();
				if (filename.equals("/")){
					filename = "/index.html";
				}
				System.out.println("requesting file: " + filename);

				//try to open and read the requested resource
				File resource = new File(resourceFolder+filename);
				//System.out.println("files"+filename);
				fileStream = new FileInputStream(resource);

			} catch(FileNotFoundException e){	//404 error
				System.out.println(e);
				//set 404 as resource
				File resource = new File(resourceFolder+"/404.html");
				try{
					fileStream = new FileInputStream(resource);
				}catch(FileNotFoundException e1){	//in case 404 doesn't exist
					System.out.println(e);
				}
				System.out.println("File couldn't be found, sending error to client");
			}
			//send whatever file we ended up with
			try{
				//write file to a byte array
				int n;
				//continue writing from fileStream onto outBuff until fileStream is out of data
				while((n = fileStream.read(outBuff) ) > 0){
					//write n bytes to output (the socket/client)
					output.write(outBuff, 0, n);
					output.flush();
				}
			}catch(IOException e){
				System.out.println(e);
			}
		}

		//close file streams
		try{
			output.close();
			input.close();
			sock.close();
			fileStream.close();
		} catch (IOException e){
			System.out.printf("Error when closing sockets and/or I/O streams: %s\n", e);
		}
		return 0;
	}

	public void quitServer(){
		try{
			System.out.println("closed listening socket!");
			server.close();
		} catch(IOException e){
			System.out.printf("Error when attempting to close Listening Socket: %s", e);
		}
	}

	//returns a substring of just "/[resource]", and ending at the newline or space
	String requestResource (){
		String filename;
		int start = inBuff.indexOf("/");
		//bad request
		if (start == -1){
			return "400.html";
		}
		int end = inBuff.indexOf(" ", start);
		char[] checklist = {' ', '\n'};
		//check for every item in checklist, in order
		for(int i = 0; end == -1 && i < checklist.length; ++i){
			end = inBuff.indexOf(checklist[i], start);
		}
		//if there's no space or newline, we consider everything after the '/' to be part of the resource
		if(end != -1){
			filename = inBuff.substring(start, end);
		} else{
			filename = inBuff.substring(start);
		}
		return filename;
	}

}
