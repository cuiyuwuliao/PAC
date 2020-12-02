package ghost;

import processing.core.*;
import java.util.*;

/**
* count as fruit, begin panic mode when steped
*/
public class SFruit extends Grid{
    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public SFruit(PImage img, int x, int y){
    	super(img, x, y);
        this.type = GridType.sFruit;
        if(sprite != null)
            this.sprite.resize(32,32);

    }
    /**
    * comsume the fruit, set image to null, reduce fruit remain, enter panic mode
    * @param game engine object
    */
    public void step(Engine game){
        if(!this.consumed){
            empty();
            this.consumed = true;
            game.map.fruitRemain -= 1;
            ModeManager.trigger(game, ModeManager.ModeType.panic);
        }
    }



}
