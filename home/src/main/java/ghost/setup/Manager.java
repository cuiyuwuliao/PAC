package ghost;


import java.util.*;
import java.io.*;
import processing.core.*;
/**
* Manager parse map and create objects for the game
*/
public class Manager{
	Config c1;
	int fps;
    int panicLength;
	List<PImage> pPics = new ArrayList<PImage>();
	HashMap<String,PImage> gPics = new HashMap<String,PImage>();
	List<Ghost> gs = new ArrayList<Ghost>();
	List<Integer> modeSwitch = new ArrayList<Integer>();

	Player p1;
	GMap map;
	Engine game;



    /**
    * Manager parse map and create objects for the game
    * @param c config object 
    * @param pPics a list of player's image source
    * @param gPics grid and ghost image sources, and their identifer in text file
    * @param others other image sources(e.g. invisible form of ghosts)
    * @param fps frame per second
    */
	public Manager(Config c, List<PImage> pPics, HashMap<String,PImage> gPics,HashMap<String,PImage> others, int fps){
        this.c1 = c;
		this.pPics = pPics;
		this.gPics = gPics;
		this.fps = fps;
        this.panicLength = c.panicLength * fps;
		parse();
		timeStamps();
        for(Ghost g : gs){
            g.img.add(others.get("frightened"));
            g.img.add(others.get("invisible"));
        }
		game = new Engine(map, p1, gs, modeSwitch, panicLength);
	}

    /**
    * parse map, store the map in a list as map source
    */
	private void parse(){
		List<Grid> mapSource = new ArrayList<Grid>();
		int column = 0;
        int row = 0;
        int[] size = {0,0};
        for(List<String> line : c1.map){
            for(String ele : line){
                int[] locXY = Basic.findXY(column,row);
                for(Map.Entry e : gPics.entrySet()){
                    String type = (String)e.getKey();
                    PImage img = (PImage)e.getValue();
                    if(ele.equals(e.getKey())){
                        Grid g = create(img, type,locXY[0],locXY[1]);
                        mapSource.add(g);
                    }
                }
                row += 1;
            }
            size[0] = row;
            row = 0;
            column += 1;
            size[1] = column;
        }
        map = new GMap(mapSource, size);
        
	}

    /**
    * create a grid object based on it's identifier in the map file
    * @param img image
    * @param type identifier in map file
    * @param x x coordinate
    * @param t y coordinate
    * @return the grid created
    */

	private Grid create(PImage img, String type, int x, int y){
		String[] wallType = {"1","2","3","4","5","6"};
    	for(String s : wallType){
    		if (type.equals(s)){
    			return new Wall(img,x,y);
    		}
    	}
    	if(type == "7"){
    		return new Fruit(img,x,y);
    	}
    	if(type == "8"){
    		return new SFruit(img,x,y);
    	}
        if(type == "s"){
            return new Soda(img,x,y);
        }
        if(type == "g"){
            Normal g1 = new Normal(img,x,y);
            g1.setSpeed(c1.speed);
            gs.add(g1);
        }
    	if(type == "a"){
    		Ambusher g1 = new Ambusher(img,x,y);
            g1.setSpeed(c1.speed);
            gs.add(g1);
    	}
    	if(type == "c"){
    		Chaser g1 = new Chaser(img,x,y);
            g1.setSpeed(c1.speed);
            gs.add(g1);
    	}
    	if(type == "i"){
    		Ignorant g1 = new Ignorant(img,x,y);
            g1.setSpeed(c1.speed);
            gs.add(g1);
    	}
    	if(type == "w"){
    		Whim g1 = new Whim(img,x,y);
            g1.setSpeed(c1.speed);
            gs.add(g1);
    	}
    	if(type == "p"){
    		p1 = new Player(pPics, x, y);
            p1.setSpeed(c1.speed);
            p1.setLives(c1.lives);
    	}
    	return new Grid(null,x,y);

	}

    /**
    * determine time for mode switching
    */
	private void timeStamps(){
		int i = 0;
        while(i < c1.modeLengths.size()){
            int ii = 0;
            int time = 0;
            while(ii <= i){
                time += c1.modeLengths.get(ii);
                ii++;
            }
            modeSwitch.add(time * fps);
            i++;
        }
    }


}

	

	