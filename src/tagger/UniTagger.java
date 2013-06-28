import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class UniTagger implements Tagger {
	/**
	 * @author BobP
	 */
	private Map<String, HashMap<String, Integer>> wtfreq;
	private String[][] sentences;
	private TaggerGUI gui;
	private Exporter ep;
	private Integer rows;
	
	/**
	 * constructor called by the gui to print the progress
	 * @param gui reference to the gui
	 */
	public UniTagger(TaggerGUI gui){
		this.gui = gui;
		init();
	}
	
	public UniTagger(){
		init();
		
		train("pali-goldstandard1.csv");
		
		tag("pali-goldstandard2.csv");
	}
	
	/**
	 * initalizes the objects used by the tagger
	 */
	private void init(){
		wtfreq = new HashMap<String, HashMap<String,Integer>>();
		sentences = new String[500][2];
		ep = new Exporter();
	}
	
	/**
	 * reads a file and takes it as base to learn the tagger
	 * @param fieName file to learn from
	 */
	@Override
	public void train(String fileName) {
		gprintln("Start training...");
		try {
			File f = new File(fileName);
			// reads Data in UTF-8
			BufferedReader in = new BufferedReader(	new InputStreamReader( new FileInputStream(f),"UTF-8"));
	 
			String str;
			String[] tmp;
			int tagfreq;
			while ((str = in.readLine()) != null) {
				tmp=str.split("	");
				for(int i=1; i<tmp.length;i++){
					tmp[0] = tmp[0].toLowerCase();
					if(!wtfreq.containsKey(tmp[0])){
						wtfreq.put(tmp[0], new HashMap<String, Integer>());
					}
					if(wtfreq.get(tmp[0]).containsKey(tmp[i])){
								
						tagfreq = wtfreq.get(tmp[0]).get(tmp[i]);
						wtfreq.get(tmp[0]).remove(tmp[i]);
						tagfreq++;
						wtfreq.get(tmp [0]).put(tmp[i], tagfreq);

					}else{
						wtfreq.get(tmp[0]).put(tmp[i],1);
					}

				}
			}
			in.close();
		} catch (IOException e) {
			gprintln("ERROR cant find/open the file "+fileName);
			gprintln("Abort training!");
			return;
		}
		gprintln("Finished training!");
	}
	
	/**
	 * tags the data in the file fileName
	 * @param fieName file to be tagged
	 */
	@Override
	public void tag(String fileName){
		gprintln("Start tagging...");
		
		int ret = tagRead(fileName);
		if(ret == 1){
			//reading in the data to tag was successful, so set the tag
			tagSet();
		}else{
			//error, can just be occur due to ioexception
			gprintln("ERROR cant find/open the file "+fileName);
			gprintln("Abort tagging!");
			return;
		}
		gprintln("Finished tagging!");
	}
	
	/**
	 * reads the data to tag in
	 * @param fileName file to be tagged
	 * @return integervalue which determines if the function was successful(1) or not(0)
	 */
	private int tagRead(String fileName){
		int pos = 0;
		File f = new File(fileName);
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader( new FileInputStream(f), "UTF-8"));
			String line;
			String[] tmp;
			while ((line = in.readLine()) != null) {
				tmp=line.split("	");
				for(int i=0;i<tmp.length;i++){
					addTokenToTag(tmp[0], pos);
				}
				pos++;
			}
			in.close();
		} catch (IOException e) {return 0;}
		return 1;
	}
	
	/**
	 * tags the data read by tagRead
	 */
	private void tagSet(){
		for(int i=0;i<sentences.length;i++){
			if(sentences[i][0]!=null){
				sentences[i][1] = getMaxTag(sentences[i][0]);
			}
		}
		printStrAr(sentences);
	}
	
	
	@Override
	public void export(String fileName) {
		fileName = fileName.substring(0,fileName.indexOf(".csv"));
		ep.exportCSV(fileName, sentences);
		gprintln("File "+fileName+".csv has been created!");
	}
	
	
	/**
	 * looks up the tag for the token "token" passed as arg with the most occurrences
	 * @param token token from which we search the most common tag
	 */
	private String getMaxTag(String token){
		String maxtag = ""; int maxcount = 0;
		boolean at_begin = true;
		
		//atleast sth in our database?
		if(wtfreq.containsKey(token)){
			//search for the tag with the most occurrences
			Iterator<Entry<String, Integer>> i = wtfreq.get(token).entrySet().iterator();
			while(i.hasNext()){
				Map.Entry<String, Integer> pairs = (Map.Entry<String,Integer>)i.next();
				if(!at_begin){
					if((int)pairs.getValue()>maxcount){
						maxtag = (String) pairs.getKey();
						maxcount = (int) pairs.getValue();
					}
				}else{
					maxtag = (String) pairs.getKey();
					maxcount = (int) pairs.getValue();
				}
					
			}
		}else{
			//nothing in our database
			return "NoTagFound";
		}
		return maxtag;
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
	
	/**
	 * stubfunction to show the output of a stringarray
	 * @param s
	 */
	private void printStrAr(String[][] s){
		for(int i=0;i<s.length;i++){
			if(s[i][0]!=null){
				gprintln(s[i][0] +"="+s[i][1]);
			}
		}
	}
	
	/**
	 * adds a token to the global array sentences and prevents the case that the array is too small
	 * by doubling its size in such a case
	 * @param token token to be written into the array
	 * @param pos position at which the token should land
	 */
	private void addTokenToTag(String token, int pos){
		if(pos > (sentences.length-1)){
			String[][] tmp = (String[][])sentences.clone();
			sentences = new String[sentences.length*2][2];
			
			for(int i=0;i<tmp.length;i++){
				sentences[i][0] = tmp[i][0];
			}
		}
		sentences[pos][0] = token;
	}
}
