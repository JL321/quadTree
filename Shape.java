import java.awt.Rectangle;

/*
 * Shape
 * v1.0
 * Author: James Liang
 * Date: 10/30/18
 * Description: Primary agent in the test program. Contains information relevant to movement and position.
 */

public class Shape {

	private int radius;
	private double xPos;
	private double yPos;
	private double dx;
	private double dy;
	
	/**
	 * Constructor for shape
	 * Initializes with initial center, radius, and speed
	 * 
	 * @param radius
	 * @param xPos
	 * @param yPos
	 * @param dx
	 * @param dy
	 */
	
	public Shape(int radius, double xPos, double yPos, double dx, double dy) {	
		this.radius = radius;
		this.xPos = xPos;
		this.yPos = yPos;
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * getRadi
	 * Returns radius of shape
	 * 
	 * @return
	 */
	
	public int getRadi() {
		return radius;
	}
	
	/**
	 * intersects 
	 * Checks intersections with nodes
	 * 
	 * @param boundingBox
	 * @return
	 */
	
	public boolean intersects(Rectangle boundingBox) {
		
		int lowerX = boundingBox.x;
		int lowerY = boundingBox.y;
		int higherX = boundingBox.x+boundingBox.width;
		int higherY = boundingBox.y+boundingBox.height;
		
		if (yPos < higherY && yPos >= lowerY) { //Uses intersections base on only the circle center, as to restrict a shape to be only in one node at a time (hence <higher rather than <=)
			if (xPos < higherX && xPos >= lowerX) {
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * intersects
	 * Overloaded method that checks if my shape intersects with another shape
	 * If compared to itself, will return false - shouldn't happen, but just a precaution
	 * 
	 * @param agent
	 * @return
	 */
	
	public boolean intersects(Shape agent) { 
		
		int centerX = (int)Math.round(agent.xPos);
		int centerY = (int)Math.round(agent.yPos);
		
		if (agent.equals(this)) {
			return false;
		}
		
		if (centerX-radius <= xPos+radius && centerX+radius >= xPos-radius) {
			if (centerY+radius >= yPos-radius && centerY-radius <= yPos+radius) {
				return true;
			}
		}
		
		return false;
		
	}

	
	/**
	 * setX
	 * Setter for x position
	 * 
	 * @param pos
	 */
	
	public void setX(double pos) {
		this.xPos = pos;
	}
	
	/**
	 * setY
	 * Setter for y position
	 * 
	 * @param pos
	 */
	
	public void setY(double pos) {
		this.yPos = pos;
	}
	
	/**
	 * setDx
	 * Setter for x speed (dx)
	 * 
	 * @param pos
	 */
	
	
	public void setDx(double dx) {
		this.dx = dx;
	}
	
	/**
	 * setDy
	 * Setter for y speed (dy)
	 * 
	 * @param dx
	 */
	
	public void setDy(double dy) {
		this.dy = dy;
	}
	
	/**
	 * getX
	 * Getter for x position
	 * 
	 * @param dy
	 */
	
	public int getX() {
		return (int)Math.round(this.xPos);
	}
	
	/**
	 * getY
	 * Getter for y position
	 *
	 * @return
	 */
	
	public int getY() {
		return (int)Math.round(this.yPos);
	}
	
	/**
	 * getDx
	 * Getter for dx (speed of x)
	 * 
	 * @return
	 */
	
	public int getDx() {
		return (int)Math.round(this.dx);
	}
	
	/**
	 * setX
	 * Getter for dy (speed of y)
	 * 
	 * @return
	 */
	
	public int getDy() {
		return (int)Math.round(this.dy);
	}
	
	/**
	 * newPos
	 * Updates position of shape
	 *
	 */
	
	public void newPos() {
		this.xPos += this.dx;
		this.yPos += this.dy;
	}
	
}
