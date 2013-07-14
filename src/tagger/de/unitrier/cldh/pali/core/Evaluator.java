package de.unitrier.cldh.pali.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Evaluator {
	
	private ArrayList<String> gsdata;
	private ArrayList<String> tgdata;
	private TaggerGUI gui;
	
	public Evaluator(String gstdFile, String taggedFile){
		
		//compare("test.csv","output.csv");
		compare(gstdFile,taggedFile);
	}
	
	public Evaluator(String gstdFile, String taggedFile, TaggerGUI taggerGUI){
		this.gui = taggerGUI;
		compare(gstdFile,taggedFile);
	}
	
	public void compare(String gstdFile, String taggedFile){
		gsdata = readDataInList(gstdFile);
		tgdata = readDataInList(taggedFile);
		
		Iterator<String> gi = gsdata.iterator();
		Iterator<String> ti = tgdata.iterator();
		
		String gnext ="",tnext="";
		
		int le = 0;			//linesEqual
		int lit = 0;		//linesInTotal
		int lnt = 0;		//linesNotTagged
		int lt = 0;			//linesTagged
		
		while(gi.hasNext() || ti.hasNext()){
			gnext = gi.next();
			tnext = ti.next();
			
			if(gnext.equals(tnext)){
				le++;
			}else{
				if(tnext.endsWith("NoTagFound")){
					lnt++;
				}
			}
			lit++;
		}
		lt = lit - lnt;
		float precision = ((float)le/(float)lt);
		float recall    = ((float)le/(float)lit);
		float f1meas    = ((precision*recall)+(precision*recall))/(precision+recall);
		float fallout   = ((float)(lt-le)/(float)lt);
		
		gprintln("\nLines not tagged:"+String.format("%11s","")+"["+String.format("%3d",lnt)+"/"+String.format("%3d",lit)+"]");
		gprintln("Lines tagged:"+String.format("%15s","")+"["+String.format("%3d",lt)+"/"+String.format("%3d",lit)+"]");
		gprintln("Lines correctly tagged:"+String.format("%5s","")+"["+String.format("%3d",le)+"/"+String.format("%3d",lt)+"]");
		gprintln("Lines not correctly tagged: ["+String.format("%3d",lt-le)+"/"+lt+"]\n");
		
		//how many lines got tagged right of the ones who got tagged 
		gprintln("Precision = "+String.format("%.5f",precision));
		//how many lines got tagged right of the whole data
		gprintln("Recall    = "+String.format("%.5f",recall));
		//harmonic mean of precision and recall
		gprintln("F1measure = "+String.format("%.5f",f1meas));
		//how many lines failed to be tagged right
		gprintln("Fallout   = "+String.format("%.5f",fallout));
	}
	
	private ArrayList<String> readDataInList(String fileName){
		ArrayList<String> tmpList = new ArrayList<String>();
		File f = new File(fileName);
		try {
			BufferedReader in = new BufferedReader(	new InputStreamReader( new FileInputStream(f),"UTF-8"));
			
			String str;
			String[] tmp;
			while ((str = in.readLine()) != null) {
				tmp=str.split("	");
				if(tmp.length==2){
					tmpList.add(tmp[0]+"_"+tmp[1]);
				}else{
					tmpList.add(tmp[0]);
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpList;
	}
	
	/**
	 * prints the output to the gui if initalized and also into the consoles
	 * @param s
	 */
	private void gprintln(String s){
		if(!(gui == null)){
			gui.println(s);
		}
		System.out.println(s);
	}
}
