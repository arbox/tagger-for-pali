package de.unitrier.cldh.pali.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ResourceManager {
	private Map<String, HashMap<String, Integer>> word_tag_frequency;
	private Map<String,Integer> unitag_count, bitag_count, tritag_count;
	private ArrayList<String> resource_lines, tag_data;
	private Set<String> poss_tags;
	
	/**
	 * the ResourceManager is the memory of our taggers, it will store the train and tagdata
	 * it also computes the statistics needed by the taggers
	 */
	public ResourceManager(){
		init();
	}
	
	private void init(){
		resource_lines = new ArrayList<String>();
		possibleTags();
	}
	
	/**
	 * computes the statistics needed to give the tagger the informations wanted
	 */
	private void compute(){
		word_tag_frequency = new HashMap<String, HashMap<String,Integer>>();
		unitag_count	= new HashMap<String,Integer>();
		bitag_count		= new HashMap<String,Integer>();
		tritag_count	= new HashMap<String,Integer>();
		
		String[] tmp = new String[0];
		String prevtags = "";
		int tag_frequency = 0, tag_count, j=0;
		
		Iterator<String> i = resource_lines.iterator();
		while(i.hasNext()){
			tmp = i.next().split("\t");
			
			
			if(tmp.length>1){//second column?
				
				//==================================count the word with a specific tag frequency [checked, works]
				//normalize
				tmp[0] = tmp[0].toLowerCase();
				
				if(!word_tag_frequency.containsKey(tmp[0])){
					word_tag_frequency.put(tmp[0], new HashMap<String, Integer>());
				}
				if(word_tag_frequency.get(tmp[0]).containsKey(tmp[1])){
							
					tag_frequency = word_tag_frequency.get(tmp[0]).get(tmp[1]);
					word_tag_frequency.get(tmp[0]).remove(tmp[1]);
					tag_frequency++;
					word_tag_frequency.get(tmp [0]).put(tmp[1], tag_frequency);
	
				}else{
					word_tag_frequency.get(tmp[0]).put(tmp[1],1);
				}
				
				
				//==================================unitag_count - counts how many times a specific tag has been seen [checked, works]
				if(unitag_count.containsKey(tmp[1])){
					tag_count = unitag_count.get(tmp[1]);
					unitag_count.put(tmp[1],++tag_count);
				}else{
					unitag_count.put(tmp[1],1);
				}
				
				//==================================bitag_count - counts how many times a specific bigram of tags has been seen [checked, works]
				
				//retrieves from position j the tags previous our current tag and delivers them in a form like "prev2_prev1_current"
				prevtags = getPrevTag(j,1);
				//eliminate bitag sequences hopping over the bounds of a sentence
				if(prevtags.indexOf("\t")>=0){
					//if so, just count
					if(bitag_count.containsKey(prevtags)){
						tag_count = bitag_count.get(prevtags);
						bitag_count.remove(prevtags);
						bitag_count.put(prevtags, ++tag_count);
					}else{
						bitag_count.put(prevtags, 1);
					}
				}
				
				//==================================tritag_count - counts how many times a specific trigram of tags has been seen [checked, works]
				prevtags = getPrevTag(j,2);
				if((prevtags.indexOf("\t")>=0) && (prevtags.indexOf("\t", prevtags.indexOf("\t")+1)>=0)){
					if(tritag_count.containsKey(prevtags)){
						tag_count = tritag_count.get(prevtags);
						tritag_count.remove(prevtags);
						tritag_count.put(prevtags, ++tag_count);
					}else{
						tritag_count.put(prevtags,1);
					}
				}
				
				
			}
			j++;
			
		}
		
	}
	
	
	/**
	 * reads a file chosen by fileName into an ArrayList and returns this list
	 * @param fileName
	 * @return
	 */
	private ArrayList<String> read(String fileName, int Columns){
		ArrayList<String> tmpList = new ArrayList<String>();
		File f = new File(fileName);
		try {
			BufferedReader in = new BufferedReader(	new InputStreamReader( new FileInputStream(f),"UTF-8"));
			
			String str, x, bwt;
			String[] tmp;
			while ((str = in.readLine()) != null) {
				tmp=str.split("\t");
				x = "";
				for(int i = 0; (i<Columns && i<tmp.length);i++) {
					if(i>0){	bwt = "\t";	}else{	bwt = "";	}
					x = x + bwt + tmp[i];
				}
				tmpList.add(x);
			}
			in.close();
		} catch (IOException e) {
			System.err.println("Couldnt find/open the File "+fileName);
			//e.printStackTrace();
		}
		return tmpList;
	}
	
	/**
	 * merges the two arraylists passed by arguments into one big 
	 * @param a1
	 * @param a2
	 * @return
	 */
	private ArrayList<String> merge(ArrayList<String> a1, ArrayList<String> a2){
		ArrayList<String> merge_tmp = new ArrayList<String>();
		
		//read the information out of a1 and fill our big arraylist with it
		Iterator<String> i1 = a1.iterator();
		while(i1.hasNext()){
			merge_tmp.add(i1.next());
		}
		//read the information out of a2 and fill our big arraylist with it
		Iterator<String> i2 = a2.iterator();
		while(i2.hasNext()){
			merge_tmp.add(i2.next());
		}
		//return our just generated big arraylist which contains now the content of a1 and a2
		return merge_tmp;
	}
	
	private void printArrayList(ArrayList<String> a){
		int counter = 0;
		Iterator<String> i = a.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
			counter++;
		}
		System.out.println("Lines="+counter);
	}
	
	private String getPrevTag(int from, int tags_needed){
		String ret = "";
		String[] tmp = null;
		
		if((from+1)>=tags_needed && ((from-tags_needed)>=0)){
			for(int i=from;i>=(from-tags_needed);i--){
				tmp = resource_lines.get(i).split("\t");
				
				//to prevent tritag sequence over sentence bounds, it gets later checked if enough tabs occurs in the returnstring
				if(tmp[0].equals("")){ return "";}
				
				if(i==from){
					ret = tmp[1];
				}else{
					//to not take empty lines
					if(tmp.length>1){
						ret = tmp[1] + "\t" + ret;
					}
				}
				
			}
		}
		return ret;
	}
	
	private void possibleTags(){
		//holds later the ranges in the array where a sentence begins and ends in form of a string
		poss_tags = new HashSet<String>(); 
		//fill with accepted tags, taken from the "Pali-TagSets.csv", used later in the isTag function
		poss_tags.add("@comma@");poss_tags.add("@point@");poss_tags.add("@quote@");poss_tags.add("@sandhi@");poss_tags.add("@semicolon@");
		poss_tags.add("absolutive");poss_tags.add("adjective");poss_tags.add("adverb");poss_tags.add("conjunction");poss_tags.add("coordinative conjunciton");
		poss_tags.add("demonstrative adverb");poss_tags.add("demonstrative personal pronoun");poss_tags.add("demonstrative pronoun");
		poss_tags.add("emphatic particle");poss_tags.add("future passive participle");poss_tags.add("indefinite personal pronoun");
		poss_tags.add("indefinite pronoun");poss_tags.add("infinitive");poss_tags.add("interrogative pronoun");poss_tags.add("noun");
		poss_tags.add("numeral");poss_tags.add("ordinal adjective");poss_tags.add("past active participle");poss_tags.add("past participle");
		poss_tags.add("past passive participle");poss_tags.add("personal pronoun");poss_tags.add("persornal pronoun");poss_tags.add("present active participle");
		poss_tags.add("present participle");poss_tags.add("present passive participle");poss_tags.add("pronoun");poss_tags.add("proper noun");
		poss_tags.add("quotation participle");poss_tags.add("relative personal pronoun");poss_tags.add("relative pronoun");poss_tags.add("verb");
	}
	
	private void setNoneTaggedToNotFound(){
		for(int i = 0; i<tag_data.size();i++){
			//no empty line & not tagged?
			if(!tag_data.get(i).equals("") && (tag_data.get(i).indexOf("\t")<0)){
				
				tag_data.set(i, tag_data.get(i)+"\tNoTagFound");
			}
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//functions needed by taggers, all public
	
	
	/**
	 * looks up the tag for the token "token" passed as arg with the most occurrences
	 * @param token token from which we search the most common tag
	 */
	public String getMaxTagByFrequencySeen(String token){
		String maxtag = ""; int maxcount = 0;
		boolean at_begin = true;
		
		//atleast sth in our database?
		if(word_tag_frequency.containsKey(token)){
			//search for the tag with the most occurrences
			Iterator<Entry<String, Integer>> i = word_tag_frequency.get(token).entrySet().iterator();
			while(i.hasNext()){
				Map.Entry<String, Integer> pairs = (Map.Entry<String,Integer>)i.next();
				if(!at_begin){
					//look if the value is higher than maxcount, if so set maxcount to its value and remember the tag
					if((int)pairs.getValue()>maxcount){
						maxtag = (String) pairs.getKey();
						maxcount = (int) pairs.getValue();
					}
				}else{
					//initalize
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
	
	
	public void addResource(String fileName){
		//read the new resource temporary into an arraylist
		ArrayList<String> tmp = read(fileName,2);
		//merge this temp arraylist with our already stored data
		resource_lines = merge(resource_lines, tmp);
		//compute the statistics again
		compute();
	}
	
	public void setTagData(String fileName){
		tag_data = read(fileName,1);
	}
	
	public ArrayList<String> getTagData(){
		return tag_data;
	}
	
	public void printTagData(){
		printArrayList(tag_data);
	}
	
	public int export(String fileName){
		setNoneTaggedToNotFound();
		
		//File f;
		BufferedWriter w = null;
		//f=new File(fileName+".csv");
		try{
			//f.createNewFile();
			w = new BufferedWriter(new FileWriter(fileName));
			for(int i = 0; i<tag_data.size();i++){
				w.write(tag_data.get(i)+"\n");
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
	
	public boolean isPossibleTag(String possibleTag){
		return poss_tags.contains(possibleTag);
	}
	public int getUnitagCount(String Unitag){
		int ret = 0;
		if(unitag_count.containsKey(Unitag)){
			ret = unitag_count.get(Unitag);
		}
		if(ret>0){return ret;}else{return 0;}
	}
	public int getBitagCount(String Bitag){
		int ret = 0;
		if(bitag_count.containsKey(Bitag)){
			ret = bitag_count.get(Bitag);
		}
		if(ret>0){return ret;}else{return 0;}
	}
	public int getTritagCount(String Tritag){
		int ret = 0;
		if(tritag_count.containsKey(Tritag)){
			ret = tritag_count.get(Tritag);
		}
		if(ret>0){return ret;}else{return 0;}
	}
	public int getTokenTagFrequency(String token, String tag){
		int ret = 0;
		if(word_tag_frequency.containsKey(token) && word_tag_frequency.get(token).containsKey(tag)){
			ret = word_tag_frequency.get(token).get(tag);
		}
		if(ret>0){return ret;}else{return 0;}
	}
	public Set<String> getPossibleTags(){
		return poss_tags;
	}
	public int getAlreadyTaggedCount(){
		int counter = 0;
		Iterator<String> i = tag_data.iterator();
		while(i.hasNext()){
			
			if((i.next().indexOf("\t")>=0)){
				counter++;
			}
		}
		return counter;
	}
}
