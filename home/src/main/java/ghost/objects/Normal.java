package ghost;

import processing.core.*;
import java.util.*;


/**
* chase target: Waka's position
* scatter target: closest corner
*/
public class Normal extends Ghost {
    
    public Normal(PImage img, int x, int y){
        super(img, x, y);
        this.type = MoveType.normal;
    }

    public Dir[] chase(Engine game){
        setTargetXY(game.p1.x,game.p1.y);
        return gsAI(game.p1.x,game.p1.y);
    }

    public Dir[] scatter(Engine game){
        Grid target = game.closeC(this);
        setTargetXY(target.x,target.y);
        return gsAI(target.x,target.y);
    }

}
