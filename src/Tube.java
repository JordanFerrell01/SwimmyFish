import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class Tube {
	int tubeX = 850, tubeY = 0, tubeWidth = 24, gapHeight = 200, speed = 2, score = 0;
	Random randHeight = new Random();
	int tubeHeight = randHeight.nextInt(299);	
	int tubeY2 = tubeHeight + gapHeight;
	
// **** DRAW TUBE IMAGE **** //	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.BLACK);
		g2d.drawRect(tubeX, 0, tubeWidth, tubeHeight);
		g2d.drawRect(tubeX, tubeY2, tubeWidth, 500 - tubeY2);
		g2d.setColor(new Color(20, 144, 67));
		g2d.fillRect(tubeX, 0, tubeWidth, tubeHeight);
		g2d.fillRect(tubeX, tubeY2, tubeWidth, 500 - tubeY2);
	}
	
// **** MOVE TUBE **** //	
	
	public void move(){
		tubeX -= speed;
		
		if (tubeX < -25) {
			score++;
			if (score % 10 == 0 && score != 0){
				speedUp();
			}
			if (score % 15 == 0 && score != 0){
				shrinkGapHeight();
			}			
			resetTube();
		}
	}
	
// **** CREATE NEW TUBE **** //	
	
	public void newHeight() {
		tubeHeight = randHeight.nextInt(299);
	}
	
	public void resetTube() {
		tubeX = 850;
		newHeight();
		tubeY2 = tubeHeight + gapHeight;
	}
	
	public void resetScore() {
		score = 0;
	}
	
	public void resetSpeed() {
		speed = 2;
	}
	
	public void shrinkGapHeight() {
		gapHeight -= 20;
	}
	
	public void speedUp() {
		speed++;
	}

	public int getX(){
		return tubeX;
	}
	
	public int getY(){
		return tubeY;
	}	
}
