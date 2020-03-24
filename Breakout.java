/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {
	
	private static final int DELAY=50;

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 3;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 1;
	

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setup();
		playGame();
	}
	
	private void playGame() {
		waitForClick();
		getBallVelocity();
		while (true) {
			moveBall();
			if (ball.getY() >= getHeight()) {
				break;
			}
			if(count == 0) {
				break;
			}
		}
	}
	
	private void getBallVelocity() {
		vy = 10.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx; 
		}
	}
	
	private void setup() {
		setupBricks();
		setupPaddle();
		setupBall();
	}
	
	private void setupBricks() {
		int startY=BRICK_Y_OFFSET;
		int x,y;
		for(int j=0;j<NBRICK_ROWS;j++) {
			for(int i=0;i<NBRICKS_PER_ROW;i++) {
				x=BRICK_SEP/2+i*(BRICK_WIDTH+BRICK_SEP);
				y=startY+j*(BRICK_HEIGHT+BRICK_SEP);
				GRect brick= new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				switch(j%10) {
				case 0:
				case 1:
					brick.setColor(Color.RED);
					break;
				case 2:
				case 3:
					brick.setColor(Color.ORANGE);
					break;
				case 4:
				case 5:
					brick.setColor(Color.YELLOW);
					break;
				case 6:
				case 7:
					brick.setColor(Color.GREEN);
					break;
				case 8:
				case 9:
					brick.setColor(Color.CYAN);
					break;
				}
				add(brick);
			}
		}
	}
	
	private void setupPaddle() {
		int x=(WIDTH-PADDLE_WIDTH)/2;
		int y=HEIGHT-PADDLE_HEIGHT-PADDLE_Y_OFFSET;
		paddle= new GRect(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
	
	private void setupBall() {
		int x= (WIDTH-2*BALL_RADIUS)/2;
		int y= (HEIGHT-2*BALL_RADIUS)/2;
		ball= new GOval(x,y,2*BALL_RADIUS,2*BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
		
	}
	
	
	public void mouseMoved(MouseEvent e) {
		if ((e.getX() < getWidth() - PADDLE_WIDTH/2) && (e.getX() > PADDLE_WIDTH/2)) {
			paddle.setLocation(e.getX() - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		}
		
	}
	
	private void moveBall() {
		ball.move(vx, vy);
		if ((ball.getX() - vx <= 0 && vx < 0 )|| (ball.getX() + vx >= (getWidth() - BALL_RADIUS*2) && vx>0)) {
			vx = -vx;
		}
		if ((ball.getY() - vy <= 0 && vy < 0 )) {
			vy = -vy;
		}

		GObject collider = getCollidingObject();
		if (collider == paddle) {
			
			if(ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS*2 && ball.getY() < getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS*2 + 4) {
				vy = -vy;	
			}
		}
		
		else if (collider != null) {
			remove(collider); 
			count--;
			vy = -vy;
		}
		pause (DELAY);
		
	}
	
	private void ballBounce() {
		
		if(ball.getX()<=0 || ball.getX()+2*BALL_RADIUS>WIDTH) {
			vx = -vx;
		}
		if(ball.getY()<=0) {
			vy = -vy;
		}
		if(ball.getY()+2*BALL_RADIUS>=HEIGHT) {
			double x= (WIDTH-feedback.getWidth())/2;
			double y= (HEIGHT-feedback.getAscent())/2;
			feedback.setFont("Monospace-20");
			feedback= new GLabel("Game Over!");
			add(feedback,x,y);
		}
	}
	
	private void checkForCollisions() {
		GObject collider=getCollidingObject();
		if(collider!=null) {
			if(collider==paddle) {
				vy = -vy;
			} else {
				vy = -vy;
				remove(collider);
				count--;
				if(count==0) {
					feedback= new GLabel("You win!");
					double x= (WIDTH-feedback.getWidth())/2;
					double y= (HEIGHT-feedback.getAscent())/2;
					feedback.setFont("Monospace-20");
					add(feedback,x,y);
				}
			}
		}
	}
	
	private GObject getCollidingObject() {
		int d= 2*BALL_RADIUS;
		if(getElementAt(ball.getX(),ball.getY())!=null) {
			return getElementAt(ball.getX(),ball.getY());
		} else if(getElementAt(ball.getX()+d,ball.getY())!=null) {
			return getElementAt(ball.getX()+d,ball.getY());
		} else if(getElementAt(ball.getX()+d,ball.getY()+d)!=null) {
			return getElementAt(ball.getX()+d,ball.getY()+d);
		} else if(getElementAt(ball.getX(),ball.getY()+d)!=null) {
			return getElementAt(ball.getX(),ball.getY()+d);
		} else {
			return null;
		}
		
	}
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private GRect paddle;
	private double lastX,vx,vy=10.0;
	private GOval ball;
	private GLabel feedback;
	private int count=NBRICK_ROWS*NBRICKS_PER_ROW;
	//private int turns=NTURNS;
}
