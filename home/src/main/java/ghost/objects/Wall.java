package ghost;

import processing.core.*;
import java.util.*;


public class Wall extends Grid{
	/**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Wall(PImage img, int x, int y){
    	super(img, x, y);
        this.type = GridType.wall;

    }

    

}
