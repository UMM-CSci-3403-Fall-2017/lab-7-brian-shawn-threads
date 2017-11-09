package echoserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}
	private void start() throws IOException {
		final Socket socket = new Socket("localhost", PORT_NUMBER);
		final InputStream socketInputStream = socket.getInputStream();
		final OutputStream socketOutputStream = socket.getOutputStream();
		Thread t1 = new Thread(){ //Create first thread for reading from the standard input
			public void run(){
				try{
					int readByte;
					while ((readByte = System.in.read()) != -1) { //read byte by byte
						socketOutputStream.write(readByte); //Send it to server
					}
					socketOutputStream.flush();
				  socket.shutdownOutput(); //Shutdown the output once we done reading
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		t1.start();
		Thread t2 = new Thread(){ //Create second thread for reading bytes from server
			public void run(){
				int socketByte;
				try {
					while ((socketByte = socketInputStream.read()) != -1) { //read byte by byte
						System.out.write(socketByte); //write it to the standard output
					}
					System.out.flush();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t2.start();
	}
}
