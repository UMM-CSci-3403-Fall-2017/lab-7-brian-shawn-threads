package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoServer {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ExecutorService cachedPool = Executors.newCachedThreadPool(); //Create the ExecutorService which will be a cached pool of threads
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

		while(true){

			/*
				Once socket accepts request from client
				We make the  instruction and an available thread from cached pool
				takes it and works.
			*/
			Socket socket = serverSocket.accept();
			Instruction instruction = new Instruction(socket); //Create the instance of Runnable called Instruction
			cachedPool.submit(instruction);//Give the instruction to the thread in the pool
		}
	}
	private class Instruction implements Runnable{
		private Socket socket;

		public Instruction(Socket socket){ //Constructor takes Socket as an argument
			this.socket = socket;
		}

		public void run(){
			try {
				InputStream	inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				int b;
				while ((b = inputStream.read())!= -1) {
					outputStream.write(b);
				}
				outputStream.flush();
				socket.close(); //
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
