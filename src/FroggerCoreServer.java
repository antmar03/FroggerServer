import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FroggerCoreServer{

	//Sprites
	private static Cat cat1,cat2;
	private static FroggerCoreServer game = null;
	//private Car car;
	
	private static RowHandler rowHandler,logRowHandler;
	final static int SERVER_PORT = 5556;
	
	public FroggerCoreServer() throws IOException {

			
	}
	
	public static void main(String[] args) throws IOException {
		//playerName = "Anthony";
		cat1 = null;
		cat2 = null;
		
		ServerSocket server = new ServerSocket(SERVER_PORT);
		System.out.println("Waiting for clients to connect...");
		
		while(true) {
			Cat catInput = null;
			int clientPort = 0;
			Socket s = server.accept();
			System.out.println("client connected");
			if(cat1 == null) {
				cat1 = new Cat();
				cat1.setPlayerNumber(1);
				catInput = cat1;
				clientPort = 5656;
			}else if(cat2 == null) {
				cat2 = new Cat();
				cat2.setPlayerNumber(2);
				catInput = cat2;
				clientPort = 5657;
			}
			
			

			ServerService sService = new ServerService(s, catInput, clientPort);
			Thread t = new Thread(sService);
			t.start();
		}
		
		
	}
	
	public static FroggerCoreServer getInstance() {
		return game;
	}
		
}
