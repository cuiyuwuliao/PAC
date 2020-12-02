package ghost;

import processing.core.*;
import java.util.*;

/**
* controlled by user input 
* has animation
*/
public class Player extends Movable{
    int lives;
    int maxLives;
    PImage currentImg = new PImage(); 
    boolean firstMove = true;

    /**
    * @param img image source for display
    * @param x x coordinate
    * @param y y coordinate
    */
    public Player(List<PImage> img, int x, int y)
    {   
        super(img, x, y);
        this.type = MoveType.player;
        try{
            this.sprite = img.get(3);
            this.currentImg = img.get(3);
        }catch(RuntimeException e){
            throw new RuntimeException("need 5 PImage: up,down,left,right,ball");
        }
        
        
    }

    /**
    * set player's lives, lives can't be less than 0
    * @param lives lives to be setted
    */
    public void setLives(int lives){
        if(lives < 0)
            lives = 0;
        this.lives = lives;
        this.maxLives = lives;
    }
    
    /**
    * set direction going, change image accordingly
    */
    public void setGoing(Dir d){
        this.going = d;
        changeImage(d);
    }

    /**
    * switch bewteen open and closed mouth
    */
    public void animation(){
        if (this.sprite == this.currentImg){
            this.sprite = img.get(4);
        }else{
            this.sprite = this.currentImg;
        }

    }

    /**
    * switch image according to the direction given
    * @param d a direction to be set
    */

    private void changeImage(Dir d){
        if(d == null)
            return;
        int index = Arrays.asList(Movable.dirs).indexOf(d);
        if(currentImg != img.get(index)){
                this.sprite = img.get(index);
                this.currentImg = sprite;
            }

    }



}