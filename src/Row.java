import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Row {
	
	private Sprite[] sprites;
	private int y,xSpacing, speedMultiplier;
	private RowType type;
	private Cat cat;
	private JLabel lCat;
	private boolean running;
	
	//-1 back 1 forward
	private int direction;
	private Thread t;
	public Row(RowType type, int num, int y, int xSpacing, int direction, int speedMultiplier, Cat cat, JLabel lCat) {
		running = false;
		this.cat = cat;
		this.lCat = lCat;
		switch(type) {
		case CAR:
			sprites = new Car[num];
			break;
		case LOG:
			sprites = new Log[num];
			break;
		}
		
		
		for(int i = 1; i <=num; i++) {
			Sprite obj = null;
			if(type == RowType.CAR) {
				obj = new Car();
			}else if (type == RowType.LOG){
				obj = new Log(cat, lCat);
			}
			obj.setCat(cat);
			obj.setCatLabel(lCat);

			sprites[i-1] = obj;
		}
		

		
		this.type = type;
		this.y = y;
		this.xSpacing = xSpacing;
		this.direction = direction;
		this.speedMultiplier = speedMultiplier;
		this.lCat = lCat;
		//startRowCollisionCheck();
	}

	
	public void addRow(Container container) {
		
		ImageIcon icon = null;
		switch(type) {
			case CAR:
				icon = new ImageIcon(getClass().getResource(Properties.carImg));
				break;
			case LOG:
				icon = new ImageIcon(getClass().getResource(Properties.logImg));
				break;
		}
		
		JLabel lCar;
		
		int offsetX = 0;
		for(Sprite sprite : sprites) {
			lCar = new JLabel();
			lCar.setIcon(icon);
			System.out.println("??");
			lCar.setSize(sprite.getWidth(), sprite.getHeight());
			sprite.setX((sprite.getX() + offsetX));
			sprite.setY(this.y);
			lCar.setLocation(sprite.getX(), sprite.getY());
			container.add(lCar);
			offsetX += sprite.getWidth() + xSpacing;
			if(type == RowType.CAR) ((Car)sprite).setCarLabel(lCar); else ((Log)sprite).setLogLabel(lCar);
			}
		}
	
	
	public Sprite[] getSprites() {
		return sprites;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void moveRow() {
		for(Sprite sprite : sprites) {
			switch(type) {
				case CAR:
					Car car = (Car)sprite;
					car.moveCar(this.direction,this.speedMultiplier);
				break;
				
				case LOG:
					Log log = (Log)sprite;
					log.move(direction, speedMultiplier);
					detectLogCollision(log);
					break;
			}
		}
		
	}
	
	public void detectLogCollision(Log log) {
		if(log.getRectangle().intersects(cat.getRectangle())) {
			cat.setY(log.getY());
			cat.setOnLog(true);
		}
	}





}

