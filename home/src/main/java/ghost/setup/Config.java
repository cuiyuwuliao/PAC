package ghost;


import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
/**
* store information from config file and map file
*/
public class Config{
	List<List<String>> map = new ArrayList<List<String>>();
	int lives, speed, panicLength;
	List<Integer> modeLengths = new ArrayList<Integer>();

	/**
	* @param filename name of the config file
    */
	public Config(String filename){
		readConfig(filename);

		
	}

	/**
    * read map file and store as list of lists
	* @param filename name of the map
    */

	private void readMap(String filename) {
		try{
			File f = new File(filename);
			Scanner scan = new Scanner(f);

			while(scan.hasNextLine()){
				String[] line_temp = scan.nextLine().split("");
				List<String> line = new ArrayList<String>();
				line = Arrays.asList(line_temp);
				map.add(line);
			}

		}catch(FileNotFoundException e){

			throw new RuntimeException("Invalid map name!");
		}
	}

	/**
    * read information from config file
	* @param filename name of the config file
    */

	private void readConfig(String filename) {
		try {
	        JSONParser parser = new JSONParser();
	        FileReader f = new FileReader(filename);
	        JSONObject data = (JSONObject) parser.parse(f);
	        this.lives = (int)(long) data.get("lives");
	        this.speed = (int)(long) data.get("speed");
	        this.panicLength = (int)(long) data.get("frightenedLength");
	        String mapName = (String) data.get("map");
	        readMap(mapName);
	        JSONArray jar = (JSONArray) data.get("modeLengths");
	        // if (jar != null) 
			for (int i=0;i<jar.size();i++){ 
				modeLengths.add((int)(long)jar.get(i));
			} 
	    } catch (IOException | ParseException e) {
	        throw new RuntimeException("Invalid jason file!");
	    }
	}

	
}

