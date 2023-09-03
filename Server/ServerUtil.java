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
	
	public String 			inBuff = "";
	public byte[] 			outBuff = new byte[MAX_SIZE];
	
	//Handles the individual client
	public int handleClient(){
			try{
				
				//setup input and output pipelines
				input = new BufferedReader(new	InputStreamReader(sock.getInputStream()));
				output = new BufferedOutputStream(sock.getOutputStream());

			} catch(IOException e){
				System.out.printf("Socket Error during client setup: %s\n", e);
				return -1;
			}
			
			try {
				//input buffer for what transmission we recieve
				inBuff = input.readLine();
				//display the transmission
				System.out.printf("[CLIENT]: %s\n", inBuff);
		
				if (inBuff.startsWith("GET")){
					String filename = "";
					FileInputStream fileStream = null;
					try{
						//make a substring of just "/[resource]", and ending at the newline or space
						try{
							//try to find a space after the filename
							filename = inBuff.substring(4, inBuff.indexOf(" ", 4));
						} catch (Exception e){
							System.out.println(e);
							try{
								//try to find a newline after filename
								filename = inBuff.substring(4, inBuff.indexOf("\n", 4));
							} catch (Exception e1){
								System.out.println(e1);
								filename = inBuff.substring(4); //TODO  deal with this
							}
						}
						//try to open and read the requested resource
						System.out.println(filename);
						File resource = new File("files"+filename);
						fileStream = new FileInputStream(resource);
						int fileSize = fileStream.available();
						//write it to a byte array
						int n;
						while((n = fileStream.read(outBuff) ) > 0){
							output.write(outBuff, 0, n);
							output.flush();
						}
						output.close();
		
					} catch(FileNotFoundException e){
						System.out.println(e);
						//output.write("404 Error", 0, 10);		TODO fix this
						System.out.println("File couldn't be found, sending error to client");
					} catch(Exception e){
						System.out.println(e);
					} finally{
						if(fileStream != null){
							fileStream.close();
						}
					}
				}
		
				else{
					//output.println("Hello Client!"); TODO fix this
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		
			//make sure to close your fileDescriptors
			try{
				output.close();
				input.close();
				sock.close();
			} catch (IOException e){
				System.out.printf("Error when closing sockets and/or I/O streams: %s\n", e);
			}
		
			//start all over again, go back to new clients
			System.out.println("\n\nwaiting for new clients...");
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

}
