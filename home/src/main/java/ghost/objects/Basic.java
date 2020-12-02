package ghost;

import processing.core.*;
import java.util.*;

/**
* base class for all map object
* contain location information
*/
public abstract class Basic{
	enum GridType{
        wall,grid,fruit,sFruit,specialItem
    }
    enum MoveType{
        normal,chaser,ambusher,ignorant,whim,player
    }
    GridType type;
    List<PImage> img = new ArrayList<PImage>();
    PImage sprite = new PImage();
    int x;
    int y; 

    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Basic(PImage img, int x, int y){
        this.x = x;
        this.y = y;
        this.sprite = img;
        this.img.add(img);
    }
    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Basic(List<PImage> img, int x, int y){
        this.x = x;
        this.y = y;
        this.sprite =img.get(0);
        this.img = img;
    }

    /**
    * @return row and col of current object
    */
    public int[] locID(){
        int[] loc = {x/16,y/16};
        return loc;
    }

    /**
    * @param column column in map file
    * @param row row in map file
    * @return XY coordination of row and col
    */
    public static int[] findXY(int column, int row){
    	int x = row * 16;
    	int y = column * 16;
    	int[] result = {x , y};
    	return result;
    }

    /**
    * set XY coordination
    * @param x x coordiate
    * @param y y coordiate
    */
    public void set(int x, int y){
    	this.x = x;
    	this.y = y;
    }



    public void draw(PApplet app){
        if(sprite != null){
            app.image(sprite,x + 8,y);
        } 
    }

    

}
