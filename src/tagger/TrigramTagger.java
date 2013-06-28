import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class TrigramTagger implements Tagger {
	private TaggerGUI gui;
	private String[][] train_data, tag_data;
	private Map<String,Integer> unitag_count, bitag_count, tritag_count, token_tag_count;
	private Set<String> poss_tags;
	private Exporter ep;
	
	public TrigramTagger(TaggerGUI taggerGUI) {
		this.gui = taggerGUI;
		init();
	}
	
	/**
	 * initalizes the objects used by the tagger
	 */
	private void init(){
		ep = new Exporter();
		train_data	= new String[500][2];
		tag_data	= new String[500][2];
		
		unitag_count	= new HashMap<String,Integer>();
		bitag_count		= new HashMap<String,Integer>();
		tritag_count	= new HashMap<String,Integer>();
		token_tag_count = new HashMap<String,Integer>();
		
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
	
	@Override
	public void train(String fileName) {
		gprintln("Start training...");
		trainRead(fileName);
		trainLookUpValues();
		gprintln("Finished training!");
	}
	@Override
	public void tag(String fileName) {
		tagRead(fileName);
		tagSet();
	}
	
	@Override
	public void export(String fileName) {
		fileName = fileName.substring(0,fileName.indexOf(".csv"));
		int ret = ep.exportCSV(fileName, tag_data);
		if(ret > -1){
			gprintln("File "+fileName+".csv has been created!");
		}else{
			gprintln("Error couldnt find the directory to save in!");
		}
		
	}
	
	/**
	 * reads the data into the array train_data, to train our tagger later on with the trainLookUp method
	 * @param fileName name of the file taken for training
	 */
	private void trainRead(String fileName){
		try {
			File f = new File(fileName);
			// reads Data in UTF-8
			BufferedReader in = new BufferedReader(	new InputStreamReader( new FileInputStream(f),"UTF-8"));
			
			String str;
			String[] tmp;
			int k = 0;
			// iterate through the document and save every line in train_data
			while ((str = in.readLine()) != null) {
				tmp=str.split("	");
				for(int i=1; i<tmp.length;i++){
					tmp[0] = tmp[0].toLowerCase();
					addtrain(k,0,tmp[0]);
					addtrain(k,1,tmp[1]);
				}
				k++;
			}
			in.close();
			
			// clean our train_data-array(delete nullslots)
			String[][] swap = new String[k][2];
			System.arraycopy(train_data, 0, swap, 0, k);
			train_data = new String[k][2];
			System.arraycopy(swap, 0, train_data, 0, k);
			
		} catch (IOException e) {
			gprintln("ERROR cant find/open the file "+fileName);
			gprintln("Abort training!");
			return;
		}
	}
	
	/**
	 * counts the amount a unigram / bigram / trigram of tags has been seen and the combination of unigram-token with a specific tag
	 */
	private void trainLookUpValues(){
		int tag_count = 0;
		String tmp;
		
		for(int i=0;i<train_data.length;i++){
			
			//skip punctuation marks and nullslots
			if(isTag(train_data[i][1])){
				
				//-----unitag_count - counts how many times a specific tag has been seen 
				if(unitag_count.containsKey(train_data[i][1])){
					tag_count = unitag_count.get(train_data[i][1]);
					unitag_count.put(train_data[i][1],++tag_count);
				}else{
					unitag_count.put(train_data[i][1],1);
				}
				
				//-----bitag_count - counts how many times a specific bigram of tags has been seen
				//retrieves the tags previous our current tag and delivers them in a form like "prev2_prev1_current"
				if(i>1){
					tmp = getPrevTag(train_data,1,i);
					
					//enough found to be a proper bitags sequence?
					if(!tmp.contains("null")){
						
						if(bitag_count.containsKey(tmp)){
							tag_count = bitag_count.get(tmp);
							bitag_count.put(tmp,++tag_count);
						}else{
							bitag_count.put(tmp, 1);
						}
					}
				}
				
				//-----tritag_count - counts how many times a specific trigram of tags has been seen
				if(i>2){
					tmp = getPrevTag(train_data,2,i);
					
					//enough found to be a proper bitags sequence?
					if(!tmp.contains("null")){
						if(tritag_count.containsKey(tmp)){
							tag_count = tritag_count.get(tmp);
							tritag_count.put(tmp,++tag_count);
						}else{
							tritag_count.put(tmp, 1);
						}
					}
				}
				//-----token_tag_count - counts how many times a specific combination of token with a  specific tag has been seen
				tmp = train_data[i][0]+"_"+train_data[i][1];
				
				if(token_tag_count.containsKey(tmp)){
					tag_count = token_tag_count.get(tmp);
					token_tag_count.put(tmp, ++tag_count);
				}else{
					token_tag_count.put(tmp, 1);
				}
			}
		}
	}
	
	/**
	 * get the previous n tag from the position "from" of the array "array"
	 * @param array array which will be taken to search in
	 * @param tags_needed how many tags needed
	 * @param from position we relative search from
	 * @return stringarray with previous n tags
	 */
	private String getPrevTag(String[][] array,int tags_needed,int from){
		int tags_seen = 0, i=1, fill_pos=0;
		String token, ret = array[from][i];
		String[] return_tags = new String[tags_needed];
		boolean endreached = false;
		
		while((tags_needed != tags_seen) && (!endreached)){
			token = array[from-i][1];
			if(isTag(token)){
				return_tags[fill_pos] = token;
				tags_seen++;
				fill_pos++;
			}
			i++;
			//was this the last possible tag?
			if((from-i<0) || (array[from-i][1]!=null && array[from-i][1].equals("part of speech"))){
				endreached = true;
				//fill the slots left open with notfound
				for(int k=(fill_pos+1);k<return_tags.length;k++){
					return_tags[k] = "null";
				}
			}
		}
		//refactor our deliv string from {1,2} to 1_2
		for(int j=0;j<return_tags.length;j++){
			ret = return_tags[j] +"_"+ ret;
		}
		return ret;
	}
	
	/**
	 * looks up if our string passed by arg is a tag
	 * @param s string to be checked
	 * @return tells us if the passed string was a tag
	 */
	private boolean isTag(String s){
		if((s!=null) && (poss_tags.contains(s))){
			return true;
		}
		
		return false;
	}
	
	/**
	 * reads the file in which we gonna tag
	 * @param fileName file to be tagged
	 */
	private void tagRead(String fileName){
		try {
			File f = new File(fileName);
			// reads Data in UTF-8
			BufferedReader in = new BufferedReader(	new InputStreamReader( new FileInputStream(f),"UTF-8"));
			
			String str;
			String[] tmp;
			int k = 0;
			// iterate through the document and save every line in tag_data
			while ((str = in.readLine()) != null) {
				tmp=str.split("	");
				for(int i=1; i<tmp.length;i++){
					tmp[0] = tmp[0].toLowerCase();
					addtag(k,0,tmp[0]);
					addtag(k,1,tmp[1]);
				}
				k++;
			}
			in.close();
			
			// clean our tag_data-array(delete nullslots)
			String[][] swap = new String[k][2];
			System.arraycopy(tag_data, 0, swap, 0, k);
			tag_data = new String[k][2];
			System.arraycopy(swap, 0, tag_data, 0, k);
			
		} catch (IOException e) {
			gprintln("ERROR cant find/open the file "+fileName);
			gprintln("Abort tagging!");
			return;
		}
	}
	
	/**
	 * defines the borders of our sentences and tags them with the help of tagSentence
	 */
	private void tagSet(){
		int sent_begin = 0, sent_end = 0;
		
		int i;
		for(i=0;i<tag_data.length;i++){
			if(tag_data[i][0]== null || tag_data[i][0].equals("null")){
				tag_data[i][0] = "";
			}
			if(tag_data[i][1]!=null && tag_data[i][1].equals("part of speech")){
				sent_end = i-1; //cause i is currently at part of speech, so i-1 is at a "." (point)
				//everything we need to know, now lets tag our sentence
				tagSentence(sent_begin, sent_end);
				//cause 1 line text, 1 line null & afterwards the start
				sent_begin = sent_end +3; 
			}
		}
		sent_end = i-1;
		tagSentence(sent_begin,sent_end);
	}

	/**
	 * tags our sentence starting at "from" and ending at "to" by splitting it into trigrams and calc the most likely tags
	 * @param from begin of the sentence
	 * @param to end of the sentence
	 */
	private void tagSentence(int from, int to){
		//split the sentence into trigrams and tag them
		int i;
		double max_poss = 0, poss =0, q, e;
		boolean begin = true;
		String[] tag_poss = new String[3];
		String[] tag_maxp = new String[3];
		Iterator<String> it_0, it_1, it_2;
		
		//Iterate through the sentence
		for(i = from; i<=(to-2);i++){
			it_0 = poss_tags.iterator();
			
			//every possible token_tag combination with every other possible token_tag combination
			while(it_0.hasNext()){
				tag_poss[0] = it_0.next();
				if(token_tag_count.containsKey(tag_data[i][0]+"_"+tag_poss[0])){
					it_1 = poss_tags.iterator();
					while(it_1.hasNext()){
						tag_poss[1] = it_1.next();
						if(token_tag_count.containsKey(tag_data[i+1][0]+"_"+tag_poss[1])){
							it_2 = poss_tags.iterator();
							while(it_2.hasNext()){
								tag_poss[2] = it_2.next();
								if(token_tag_count.containsKey(tag_data[i+2][0]+"_"+tag_poss[2])){
				
									//calc our possibility
									e = (e(tag_data[i][0],tag_poss[0]) * e(tag_data[i+1][0],tag_poss[1]) * e(tag_data[i+2][0],tag_poss[2]));
									q = q(tag_poss[0],tag_poss[1],tag_poss[2]);
									//if its more likely change the tags
									if(e*q>max_poss){
										max_poss = e*q;
										tag_data[i][1]   = tag_maxp[0];
										tag_data[i+1][1] = tag_maxp[1];
										tag_data[i+2][1] = tag_maxp[2];
									}
								}
							}
						}
					}
				}
			}
		}
		//if some trigrams couldnt get properly tagged just take the most seen
		tagNotTaggedWithMostSeen(from, to);
		//print our tag_data, the spam slows our application a bit
		printStrArr(tag_data);
	}
	
	/**
	 * tags the slots not already tagged by our trigramtagger with the most seen tag for a each token 
	 * @param from begin of our sentence to be checked
	 * @param to end of our sentence
	 */
	private void tagNotTaggedWithMostSeen(int from, int to){
		Iterator<String> it;
		String mostSeenTag = "", currentPossTag = "";
		Integer timesSeen = 0;
		
		//iterate over every token of our sentence
		for(int i=from;i<=to;i++){
			
			
			//look if its not tagged
			if(tag_data[i][1]==null){ tag_data[i][1]=""; }
			
			if(tag_data[i][1].equals("null") || tag_data[i][1].equals("")){
				//not tagged so just look up the most seen tag or it, and set it to that
				it = poss_tags.iterator();
				timesSeen = 0;
				while(it.hasNext()){
					//sadly need to test with every existing tag since our datastructure isnt formed that efficient
					currentPossTag = it.next();
					if(token_tag_count.containsKey(tag_data[i][0]+"_"+currentPossTag)){
						//test if this token tag combination is more likely then the one we found earlier
						if(token_tag_count.get(tag_data[i][0]+"_"+currentPossTag)>timesSeen){
							timesSeen   = token_tag_count.get(tag_data[i][0]+"_"+currentPossTag);
							mostSeenTag = currentPossTag;
						}
					}
				}
				tag_data[i][1] = mostSeenTag;
			}
		}
	}
	
	/**
	 * calcs the probability of seeing the tag s immediately after the bigram of tags (u, v)
	 * @param u tag
	 * @param v tag
	 * @param s tag
	 * @return probabilty
	 */
	private double q(String u, String v, String s){
		double ret = 0;
		if(bitag_count.containsKey(u+"_"+v) && tritag_count.containsKey(u+"_"+v+"_"+s)){
			ret = ((double)tritag_count.get(u+"_"+v+"_"+s)/(double)bitag_count.get(u+"_"+v));
		}
		return ret;
	}
	
	/**
	 * calcs the probability of seeing token x paired with tag s
	 * @param x token
	 * @param s tag
	 * @return probability
	 */
	private double e(String x, String s){
		double ret = 0;
		ret = ((double)token_tag_count.get(x+"_"+s) / (double)unitag_count.get(s));
		
		return ret;
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
	 * prints out our array passed by arg
	 * @param s array to be printed
	 */
	private void printStrArr(String[][] s){
		for(int i=0;i<s.length;i++){
				gprintln(s[i][0] +"="+s[i][1]);
		}
	}
	
	/**
	 * adds a value to the position i,j into the global array train_data
	 * @param i row
	 * @param j column
	 * @param value added value
	 */
	private void addtrain(int i, int j, String value){
		try{
			train_data[i][j] = value;
		}catch(ArrayIndexOutOfBoundsException e){
			String[][] tmp = new String[train_data.length][2];
			//copy array into tmp
			System.arraycopy(train_data, 0, tmp, 0, train_data.length);
			//make the array bigger
			train_data = new String[train_data.length*2][2];
			//copy tmp back into our bigger array
			System.arraycopy(tmp, 0, train_data, 0, tmp.length);
			//add our value we couldnt add
			addtrain(i,j,value);
			System.err.println("note - had to double array size (from "+tmp.length+" to "+train_data.length+")");
		}
	}
	
	/**
	 * adds a value to the position i,j into the global array tag_data
	 * @param i row
	 * @param j column
	 * @param value added vlaue
	 */
	private void addtag(int i, int j, String value){
		try{
			tag_data[i][j] = value;
		}catch(ArrayIndexOutOfBoundsException e){
			String[][] tmp = new String[tag_data.length][2];
			//copy array into tmp
			System.arraycopy(tag_data, 0, tmp, 0, tag_data.length);
			//make the array bigger
			tag_data = new String[tag_data.length*2][2];
			//copy tmp back into our bigger array
			System.arraycopy(tmp, 0, tag_data, 0, tmp.length);
			//add our value we couldnt add
			addtrain(i,j,value);
			System.err.println("note - had to double array size (from "+tmp.length+" to "+tag_data.length+")");
		}
	}
}
