package ghost;

import java.util.*;
import java.io.*;
import processing.core.*;


class Resources extends PApplet {
    Config c1;
    int fps = 60;
    List<PImage> pPics = new ArrayList<PImage>();
    HashMap<String,PImage> gPics = new HashMap<String,PImage>();
    HashMap<String,PImage> otherPics = new HashMap<String,PImage>();
    Manager m;
    Engine game;

    Normal normal;
    Ambusher ambusher;
    Chaser chaser;
    Ignorant ignorant;
    Whim whim;

    public Resources(String filename){
    	Config c1 = new Config("src/test/resources/" + filename);
    	PImage test = null;

        
        pPics.add(test);
        pPics.add(test);
        pPics.add(test);
        pPics.add(test);
        pPics.add(test);
        
        gPics.put("0",null);
        gPics.put("1",test);
        gPics.put("2",test);
        gPics.put("3",test);
        gPics.put("4",test);
        gPics.put("5",test);
        gPics.put("6",test);
        gPics.put("7",test);
        gPics.put("p",test);
        gPics.put("g",test);
        gPics.put("a",test);
        gPics.put("c",test);
        gPics.put("i",test);
        gPics.put("w",test);
        gPics.put("8",test);
        gPics.put("s",test);

        
        otherPics.put("frightened",test);
        otherPics.put("invisible",test);


        m = new Manager(c1, pPics, gPics,otherPics, fps);
        game = m.game;

        for(Ghost gs : game.gs){
            if(gs.type == Basic.MoveType.normal)
                normal = (Normal)gs;
            else if(gs.type == Basic.MoveType.ambusher)
                ambusher = (Ambusher)gs;
            else if(gs.type == Basic.MoveType.chaser)
                chaser = (Chaser)gs;
            else if(gs.type == Basic.MoveType.ignorant)
                ignorant = (Ignorant)gs;
            else if(gs.type == Basic.MoveType.whim)
                whim = (Whim)gs;
        }

    }




}
