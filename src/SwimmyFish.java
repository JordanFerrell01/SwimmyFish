import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Random;

//import java.util.Timer;
//import java.util.TimerTask;

public class SwimmyFish extends Applet implements Runnable, KeyListener{
	final int HEIGHT = 500, WIDTH = 800;
	Thread thread;
	int fishX = 75, fishY = 240, fishYVel, fishWidth = 30, fishHeight = 20; 
	int bubblesX = 400, bubblesY = 550;
	int score = 0, hiScore = 0;
	Image SwimmyFish, TubeTop, TubeBottom;
	boolean fishAlive = false, bubbleAlive = false, fishSwimming = false, fishFloating = false,	firstImage = true;;
	Random rand = new Random();
	Tube tube;
//	Timer clock;
	
	public void init() {	
		setSize(WIDTH, HEIGHT);
		this.addKeyListener(this);
		tube = new Tube();
		
//		SwimmyFish = getImage(getDocumentBase(), "SwimmyFish1.png");
		SwimmyFish = getImage(getClass().getResource("/Resources/SwimmyFish1.png"));
		TubeTop = getImage(getClass().getResource("/Resources/TubeTop.png"));
		TubeBottom = getImage(getClass().getResource("/Resources/TubeBottom.png"));
		
//		clock = new Timer();
		
		thread = new Thread(this);
		thread.start();		
	}
	
/// **** IMAGES **** ///
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		/* JRF - PAINT BACKGROUND */
		Color teal = new Color(35, 131, 177);
	    GradientPaint blackToTeal = new GradientPaint(420, 520, Color.BLACK, 380, -20,
	    		teal);
	    g2d.setPaint(blackToTeal);
	    g2d.fillRect(0, 0, WIDTH, HEIGHT);		
		
		/* JRF - PAINT TITLE SCREEN */
		if (!fishAlive) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 45));
			g2d.drawString("Swimmy Fish", 260, 75);
			
			g2d.setFont(new Font("Arial", Font.BOLD, 18));
			g2d.drawString("Press SPACE to swim down...", 280, 250);
			g2d.drawString("Press ENTER to start game...", 283, 270);
		}
		
		g2d.setColor(Color.WHITE);
		//DEBUG CODE
//		g2d.setFont(new Font("Times New Roman", Font.PLAIN, 18));
//		g2d.drawString("Fish Y: " + fishY, 25, 25);
//		g2d.drawString("Fish ALIVE: " + fishAlive, 25, 40);
//		g2d.drawString("Tube Y: " + tube.tubeY, 25, 55);
//		g2d.drawString("Tube HEIGHT: " + tube.tubeHeight, 25, 70);
//		g2d.drawString("Tube Gap: " + tube.gapHeight, 25, 85);
//		g2d.drawString("Tube Y2: " + tube.tubeY2, 25, 100);
//		g2d.drawString("Front Fish: " + (fishX + fishWidth), 25, 115);
//		g2d.drawString("Tube X: " + tube.tubeX, 25, 130);
		
		
		/* JRF - PAINT SCORE BOARD */
		g2d.setFont(new Font("Arial", Font.BOLD, 22));
		g2d.drawString("SCORE: " + tube.score, 660, 50);
		g2d.drawString("HISCORE: " + hiScore, 639, 75);
		
		/* JRF - PAINT FISH */
		//border box
			//g2d.fillRect(fishX, fishY, fishWidth, fishHeight);
		g2d.drawImage(SwimmyFish, fishX, fishY, this);
		
		/* JRF - DRAW BUBBLES */
		g2d.drawOval(bubblesX, bubblesY, 40, 40);
		g2d.drawOval(bubblesX + 35, bubblesY - 3, 8, 8);
		g2d.drawOval(bubblesX - 15, bubblesY + 31, 16, 16);		
		g2d.drawOval(bubblesX - 6, bubblesY + 50, 21, 21);
		g2d.drawOval(bubblesX - 12, bubblesY + 73, 8, 8);
		
		/* JRF - INSTANTIATE TUBE */
		tube.draw(g2d);
		
		/* JRF - PAINT TUBE CAPS */
		g2d.drawImage(TubeBottom, tube.tubeX-2, tube.tubeHeight-6, this);
		g2d.drawImage(TubeTop, tube.tubeX-4, tube.tubeY2-2, this);
	}
	
/// **** MOVEMENT **** ///	
	
	/* JRF - CONSISTENTLY MOVE FISH TOWARD THE TOP OF THE SCREEN*/
	public void floating() {
		if (fishSwimming){
			fishYVel += 1;
		} else {
			if (tube.score >= 25){
				fishYVel = -2;
			} else {
				fishYVel = -1;
			}
		}
		fishY += fishYVel;
		
//		/* JRF - MOVE THE FISH TAIL */
//		if (fishAlive) {
//			clock.schedule(new TimerTask()
//			{
//				public void run() {
//					//stuff that will happen
//					if (firstImage){
//						SwimmyFish = getImage(getDocumentBase(), "SwimmyFish2.png");
//						firstImage = false;
//					} else {
//						SwimmyFish = getImage(getDocumentBase(), "SwimmyFish1.png");
//						firstImage = true;
//					}
//					repaint();
//				}
//		
//			}, 1000, 500);
//		}
		
		/* JRF - MOVE THE FISH TAIL VERSION 2 - PREVIOUS VERSION TOO TAXING??? */
		if (fishAlive){
			if (tube.tubeX % 3 == 0){
				SwimmyFish = getImage(getClass().getResource("/Resources/SwimmyFish2.png"));
			} else {
				SwimmyFish = getImage(getClass().getResource("/Resources/SwimmyFish1.png"));
			}
		}
	}
	
	/* - JRF - SHOULD THE FISH BE DEAD?  TOP/BOTTOM SCREEN OR TUBE */
	public void checkCrash() {
		//fish hits the top of the screen
		if (fishY < 1) {
			fishAlive = false;
			fishY = 240;
		}
		
		//fish moves off the bottom of the screen
		if (fishY > HEIGHT) {
			fishAlive = false;
			fishY = 240;
		}		
		
		//tube is in the same X space as the fish
		if (tube.tubeX < 105 && tube.tubeX > 75) {
			//fish top is lower than the bottom of top tube and fish bottom is higher than the top of bottom tube (fish is in the gap)
			if ((fishY > tube.tubeHeight) && (fishY + fishHeight < tube.tubeY2)){
				fishAlive = true;
			} else {
				fishAlive = false;
			}
		} 
					
		/* JRF - IF FISH DIES RESET EVERYTHING, CHECK HIGH SCORE */
		if (!fishAlive){
			if (tube.score > hiScore){
				hiScore = tube.score;
			}
			tube.resetTube();
			tube.resetScore();
			tube.resetSpeed();
		}
	}
	
	/* JRF - CREATE RANDOM BUBBLES EVERY 5 TUBES */
	public void createBubbles() {
		if (tube.score % 5 == 0 && tube.score != 0){
			bubbleAlive = true;
		}
		
		if (bubbleAlive) {
			bubblesY -= 1;
		}
		
		if (bubblesY < -50){
			bubbleAlive = false;
			bubblesX = rand.nextInt(700);
			bubblesY = 550;
		}
	}
	
	public void update(Graphics g){
		paint(g);
	}	
	
	public void run() {
		for(;;) {  //infinite loop
			if (fishAlive){
				tube.move();
				floating();
				checkCrash();
				createBubbles();
			}
			
			repaint(); //used by the Applet
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
// **** ACTION LISTENERS **** //
	
	public void keyTyped(KeyEvent e) {	
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			fishAlive = true;
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			fishSwimming = true;
			fishFloating = false;
		} else {
			fishFloating = true;
			fishSwimming = false;
		}	
	}

	public void keyReleased(KeyEvent e) {	
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			fishAlive = true;
		}	
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			fishFloating = true;
			fishSwimming = false;
		}
	}
}
