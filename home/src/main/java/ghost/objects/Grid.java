package ghost;

import processing.core.*;
import java.util.*;


/**
* this grid refers to '0' in map file
* trigger effect by step() method 
*/
public class Grid extends Basic{
    boolean consumed = false;
    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Grid(PImage img, int x, int y){
    	super(img, x, y);
        this.type = GridType.grid;

    }
    /**
    * rest to original status 
    */
    public void reset(){
        this.consumed = false;
        this.sprite = this.img.get(0);

    }
    /**
    * does nothing by defualt
    * @param game engine object
    */
    public void step(Engine game){}

    /**
    * set display image to null
    */
    public void empty(){
        this.sprite = null;
    }

    /**
    * @return gridType
    */
    public GridType getType(){
    	return this.type;
    }



    

}
