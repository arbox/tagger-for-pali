package de.unitrier.cldh.pali.core;
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
	public int exportCSV(String fileName,Map<String, String> data){
		File f;
		BufferedWriter w = null;
		
		f=new File(fileName+".csv");
		try {
			
			f.createNewFile();
			w = new BufferedWriter(new FileWriter(fileName+".csv"));
			
			Iterator<Entry<String, String>> it = data.entrySet().iterator();
		    while (it.hasNext()) {
				Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
				if(pairs.getValue()!=null) {
					w.write(pairs.getKey() + "	" + pairs.getValue()+"\n");
		        }else{
		        	w.write(pairs.getKey());
		        }
		    }
		    
		} catch (IOException e) {
			System.out.println("ERROR cant find/open the file "+fileName);
			return -1;
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
		return 0;
	}
	
	public int exportCSV(String fileName, String[][] data, int linesToExport){
		File f;
		BufferedWriter w = null;
		f=new File(fileName+".csv");
		int minrows = 0;
		if(data.length<linesToExport){
			minrows = data.length;
		}else{
			minrows = linesToExport;
		}
		try{
			f.createNewFile();
			w = new BufferedWriter(new FileWriter("output.csv"));
			
			for(int i=0;i<minrows;i++){
				if(data[i][0]!=null){
					if(data[i][1]!=null){
						w.write(data[i][0]+ "	" +data[i][1]+"\n");
					}else{
						w.write(data[i][0]+"\n");
					}
				}else{
					w.write("\n");
				}
			}
		} catch(IOException e){
			System.out.println("ERROR cant find/open the file "+fileName);
			return -1;
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
		return 0;
	}
}
