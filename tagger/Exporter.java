import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Exporter {
	/**
	 * exports the data provided as input into the chosen format
	 * @see requires GSON as referenced lib
	 */
	public Exporter(){
	}
	/**
	 * creates a csv file with the data provided by the map data
	 * @param fileName name of the created File
	 * @param data data printed into the csv File
	 */
	public void exportCSV(String fileName,Map<String, String> data){
		File f;
		BufferedWriter w = null;
		
		f=new File(fileName+".csv");
		try {
			
			f.createNewFile();
			w = new BufferedWriter(new FileWriter(fileName+".csv"));
			
			Iterator<Entry<String, String>> it = data.entrySet().iterator();
		    while (it.hasNext()) {
				Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
		        w.write(pairs.getKey() + "	" + pairs.getValue()+"\n");
		    }
		    
		} catch (IOException e) {
			System.out.println("ERROR cant find/open the file "+fileName);
		} finally {
			try {
                if (w != null) {
                    w.flush();
                    w.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
	}
	/**
	 * creates a json file with the data provided by the map data
	 * @param fileName name of the created file
	 * @param data data printed into the json file
	 */
	public void exportJSON(String fileName,Map<String, String> data){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		 
		// convert java object to JSON format, and returned as JSON formatted string
		String json = gson.toJson(data);
	 
		File f = new File(fileName+".json");
		BufferedWriter w = null;
		try {
			
			f.createNewFile();
			w = new BufferedWriter(new FileWriter(fileName+".json"));
			w.write(json);
		
		} catch (IOException e) {
			System.out.println("ERROR cant find/open the file "+fileName);
		} finally {
			try {
                if (w != null) {
                    w.flush();
                    w.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
	}
}
