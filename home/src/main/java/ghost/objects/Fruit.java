package ghost;

import processing.core.*;
import java.util.*;

/**
* reduce fruit remain when steped, valid step only hanppen once
*/
public class Fruit extends Grid{
    

    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Fruit(PImage img, int x, int y){
    	super(img, x, y);
        this.type = GridType.fruit;
    }

    /**
    * comsume the fruit, set image to null, reduce fruit remain
    * @param game engine object
    */
    public void step(Engine game){
        if(!this.consumed){
            empty();
            this.consumed = true;
            game.map.fruitRemain -= 1;

        }
    }


}
