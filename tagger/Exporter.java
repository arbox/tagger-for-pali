import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


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
	
	public void exportCSV(String fileName, String[][] data){
		File f;
		BufferedWriter w = null;
		
		f=new File(fileName+".csv");
		try{
			f.createNewFile();
			w = new BufferedWriter(new FileWriter(fileName+".csv"));
			
			for(int i=0;i<data.length;i++){
				if(data[i][0]!=null){
					w.write(data[i][0]+ "	" +data[i][1]+"\n");
				}
			}
		} catch(IOException e){
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
