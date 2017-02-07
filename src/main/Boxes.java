/**
 * 
 */
package main;

/**
 * @author Harrison
 *
 */
public class Boxes extends ChessBoardGUI {
	private int x;
	private int y;
	
	
	//Creates a box to be added to the GUI
	public Boxes(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
