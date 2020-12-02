package ghost;

import processing.core.*;
import java.util.*;

/**
* does not count as fruit, begin soda mode when steped
*/
public class Soda extends Grid{
    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Soda(PImage img, int x, int y){
    	super(img, x, y);
        this.type = GridType.specialItem;

    }
    /**
    * comsume soda can, set image to null, enter soda mode
    * @param game engine object
    */
    public void step(Engine game){
        if(!this.consumed){
            empty();
            this.consumed = true;
            ModeManager.trigger(game, ModeManager.ModeType.sodaTime);

        }
    }



}
