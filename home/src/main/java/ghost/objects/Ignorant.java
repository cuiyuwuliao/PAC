package ghost;

import processing.core.*;
import java.util.*;
/**
* chase target: If more than 8 units away from Waka (straight line distance), target location is Waka. Otherwise, target location is bottom left corner
* scatter target: Bottom left corner
*/
public class Ignorant extends Ghost{

    public Ignorant(PImage img, int x, int y){
        super(img, x, y);
        this.type = MoveType.ignorant;
    }


    public Dir[] chase(Engine game){
    	double distance = game.distance(this.x, this.y, game.p1.x, game.p1.y);
    	if (distance/16 > 8){
    		return scatter(game);
    	}else{
    		setTargetXY(game.p1.x,game.p1.y);
        	return gsAI(game.p1.x,game.p1.y);
    	}
    	
    }

    public Dir[] scatter(Engine game){
        Grid target = game.map.corners.get(2);
        setTargetXY(target.x,target.y);
        return gsAI(target.x, target.y);
    }




}
