import java.awt.Container;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.swing.JLabel;


//TODO Make cars all move on one thread instead of each car having their own thread

public class RowHandler implements Runnable{

	//Row rowsArray[];
	private Boolean carsMoving;
	private int y;
	private Row rowArray[];
	private Thread thread;
	private OutputStream outStream;
	private PrintWriter out;
	
	public RowHandler(OutputStream outStream, PrintWriter out) {
		this.carsMoving = false;
		this.outStream = outStream;
		this.out = out;
	}
	
	
	public void startMovingCars() {
		if(!this.carsMoving) {
			thread = new Thread(this, "Cars Thread");
			thread.start();
		}
	}
	
	
	public void stopMovingCars() {
		this.carsMoving = false;
	}


	@Override
	public void run() {
		this.carsMoving = true;
		
		while(this.carsMoving) {
			String commandOut = "MOVE";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
			
			
			
			/*Cat cat = rowArray[1].getSprites()[1].getCat();
			if(cat.getY() <= 400 && !cat.getOnLog()) {
				if(cat.getY() <=3 ) {
					FroggerCoreServer.getInstance().endGame(true);
				}else {
					FroggerCoreServer.getInstance().endGame(false);
				}
				
			}*/
			
			//Pause so movement is smooth
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
