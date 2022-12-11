import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class ServerService implements Runnable{

		int CLIENT_PORT = 5656;

		private Socket s;
		private Scanner in;
		private Cat cat;

		RowHandler rHandler=null,rHandler2 = null;
		
		public void sendCommandToClients(String command) throws UnknownHostException, IOException {
			int port2 = 0;
			if(CLIENT_PORT == 5656) {
				port2 = 5657;
			}else {
				port2 = 5656;
			}
			
			
			Socket s = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
			
			System.out.println("Sending: " + command);
			out.println(command);
			out.flush();

			
			//send a response to second client
			s = new Socket("localhost", port2);
			
			//Initialize data stream to send data out
			outstream = s.getOutputStream();
			out = new PrintWriter(outstream);
			
			System.out.println("Sending: " + command);
			out.println(command);
			out.flush();

		}
		
		
		public ServerService (Socket aSocket, Cat catInput, int clientPort) {
			this.s = aSocket;
			this.cat = catInput;
			this.CLIENT_PORT = clientPort;
		}
		
		public void run() {
			
			try {
				in = new Scanner(s.getInputStream());
				processRequest( );
			} catch (IOException e){
				e.printStackTrace();
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		//processing the requests
		public void processRequest () throws IOException {
			//if next request is empty then return
			while(true) {
				if(!in.hasNext( )){
					return;
				}
				String command = in.next();
				if (command.equals("Quit")) {
					return;
				} else {
					executeCommand(command);
				}
			}
		}
		
		public void executeCommand(String command) throws IOException {
			int port2 = 0;
			if(CLIENT_PORT == 5656) {
				port2 = 5657;
			}else {
				port2 = 5656;
			}
			
			if(command.equals("END")) {
				String worl = in.next();
				switch(worl) {
				case "LOSE":
					rHandler.stopMovingCars();
					rHandler2.stopMovingCars();
					ConnectToDatabase.getInstance(cat, "Anthony").setPlayerScore("Anthony", ConnectToDatabase.getInstance(cat, "Anthony").getPlayerScore("Anthony") - 50);
					sendCommandToClients("UPDATE " + ConnectToDatabase.getInstance(cat, "Anthony").getPlayerScore("Anthony"));
				break;
				
				case "WIN":
					rHandler.stopMovingCars();
					rHandler2.stopMovingCars();
					ConnectToDatabase.getInstance(cat, "Anthony").setPlayerScore("Anthony", ConnectToDatabase.getInstance(cat, "Anthony").getPlayerScore("Anthony") + 50);
					sendCommandToClients("UPDATE " + ConnectToDatabase.getInstance(cat, "Anthony").getPlayerScore("Anthony"));
					
				break;
				}
				
			}
			
			if(command.equals("GET")) {
				
				//send a response
				Socket s2 = new Socket("localhost", CLIENT_PORT);
				
				//Initialize data stream to send data out
				OutputStream outstream = s2.getOutputStream();
				PrintWriter out = new PrintWriter(outstream);

				String commandOut = "SET " + cat.getPlayerNumber();
				out.println(commandOut);
				out.flush();
					
				s2.close();
			}
			
			
			if (command.equals("START")) {
				//send a response
				Socket s = new Socket("localhost", CLIENT_PORT);
				
				//Initialize data stream to send data out
				OutputStream outstream = s.getOutputStream();
				PrintWriter out = new PrintWriter(outstream);
				rHandler = new RowHandler(outstream, out);
				rHandler.startMovingCars();
				
				//send a response
				Socket s2 = new Socket("localhost", port2);
				
				//Initialize data stream to send data out
				outstream = s2.getOutputStream();
				out = new PrintWriter(outstream);
				rHandler2 = new RowHandler(outstream, out);
				rHandler2.startMovingCars();
			}
			
			if(command.equals("STOP")) {
				//send a response
				Socket s = new Socket("localhost", CLIENT_PORT);
				
				//Initialize data stream to send data out
				OutputStream outstream = s.getOutputStream();
				PrintWriter out = new PrintWriter(outstream);
				rHandler.stopMovingCars();
				
				//send a response
				Socket s2 = new Socket("localhost", port2);
				
				//Initialize data stream to send data out
				outstream = s2.getOutputStream();
				out = new PrintWriter(outstream);
				rHandler2.stopMovingCars();
			}
			
			if ( command.equals("PLAYER")) {
				int playerNo = in.nextInt();
				String playerAction = in.next();
				System.out.println("Player "+playerNo+" moves "+playerAction);
				
				if(cat.getPlayerNumber() == playerNo) {
					switch(playerAction) {
						case "UP":
							cat.setY(cat.getY() - Properties.STEP);
						break;
						case "DOWN":
							cat.setY(cat.getY() + Properties.STEP);
						break;
						case "LEFT":
							cat.setX(cat.getX() - Properties.STEP);
						break;
						case "RIGHT":
							cat.setX(cat.getX() + Properties.STEP);
						break;
					}
					
					String commandOut = "PLAYER " +playerNo+ " " +cat.getX()+ " " +cat.getY()+ "\n";
					this.sendCommandToClients(commandOut);
				
				}


			}
		}
}
