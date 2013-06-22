import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class UniTagger implements Tagger {
	/**
	 * @author BobP
	 */
	private Map<String, HashMap<String, Integer>> wtfreq;
	private Map<String, Integer> tagmap;
	private Map<String, String> taggeddata;
	private Set<String> sents;
	private TaggerGUI gui;
	private Exporter ep;
	
	public UniTagger(TaggerGUI gui){
		this.gui = gui;
		
		init();
	}
	public UniTagger(){
		init();
		
		train("pali-goldstandard1.csv");
		
		//printMap(wtfreq);
		
		tag("pali-goldstandard2.csv");
	}
	
	private void init(){
		wtfreq = new HashMap<String, HashMap<String,Integer>>();
		sents  = new HashSet<String>();
		
		taggeddata = new HashMap<String,String>();
		ep = new Exporter();
	}
	@Override
	public void train(String fileName) {
		System.out.println("Start training...");
		guiprintln("Start training...");
		try {
			File f = new File(fileName);
			/* Hat noch ein Problem da jegliche Umwandelung (von byte zu  String in UTF-8/ISO./USASCII etc) dazu führen das manche Characters 
			 * zu ? werden, dieses Verhalten trat bisher nur mit den goldstandard.csv-Daten auf
			 * */
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
			System.out.println("ERROR cant find/open the file "+fileName);
			guiprintln("ERROR cant find/open the file "+fileName);
			guiprintln("Abort training!");
			return;
		}
		System.out.println("Finished training!");
		guiprintln("Finished training...");
	}
	private void printMap(Map<String,String> mp) {
		Iterator<Entry<String, String>> it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			guiprintln(pairs.getKey() + " = " + pairs.getValue());
		}
	}
	@Override
	public void tag(String fileName) {
		System.out.println("Start tagging...");
		guiprintln("Start tagging...");
		//Lese die zu taggende Daten ein
		int ret = tagRead(fileName);
		if(ret == 1){
			//Tagge die Daten
			tagSet();
			System.out.println("Finished tagging!");
			guiprintln("Finished tagging!");
		}
	}
	
	private int tagRead(String fileName){
		File f = new File(fileName);
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader( new FileInputStream(f), "UTF-8"));
			String str;
			String[] tmp;
			String sen = "";
			while ((str = in.readLine()) != null) {
				tmp=str.split("	");
				if(tmp[0].equals("text")){
					//Entferne alle mehrfachen Spaces
					sen = sen.replaceAll(" +", " ");
					//Merke den Satz
					sents.add(sen);
					//Reset um den naechsten zu lesen
					sen = "";
				}else{
					//Nehme Satzzeichen nicht mit in den Satz auf
					if(!(tmp[0].equals(" ") || tmp[0].equals(".")|| tmp[0].equals("!")|| tmp[0].equals("?")|| tmp[0].equals(",")|| tmp[0].equals("?"))){
						//Baue den Satz zusammen, Wort fuer Wort
						sen  = sen +"  "+ tmp[0];
					}
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("ERROR cant find/open the file "+fileName);
			guiprintln("ERROR cant find/open the file "+fileName);
			guiprintln("Abort tagging!");
			return 0;
		}
		return 1;
	}
	
	private void tagSet() {
		Iterator<String> it=sents.iterator();
		while(it.hasNext()){
          String value=(String)it.next();
          String[] tmp;
          tmp=value.split(" ");
          //Iteriere ueber jedes Wort im Satz und gib dem Wort falls bekannt das haeufigste gesehene Tag
          for(int i=0; i<tmp.length;i++){
				if(wtfreq.containsKey(tmp[i])){
					//Suche haeufigstes Tag (provisorisch)
					tagmap = wtfreq.get(tmp[i]);
					int step_i = 0, maxcount=0;
					String maxtag = "";
					Iterator<Entry<String, Integer>> it2 = tagmap.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry<String,Integer> pairs = (Map.Entry<String,Integer>)it2.next();
						if(step_i == 0) {
							maxtag = (String) pairs.getKey();
							maxcount = (int) pairs.getValue();
						}else{
							if(((int)pairs.getValue())>maxcount){
								maxtag = (String) pairs.getKey();
								maxcount = (int) pairs.getValue();
							}
						}
					}
					//Setze Tag
					//tmp[i] = tmp[i]+" --- "+maxtag;
					taggeddata.put(tmp[i],maxtag);
				}
		  }
          //Gib getaggte Seq aus
          printMap(taggeddata);
        }
	}
	@Override
	public void export(String fileName) {
		fileName = fileName.substring(0,fileName.indexOf("."));
		ep.exportCSV(fileName, taggeddata);
		guiprintln("File "+fileName+".csv has been created!");
	}
	
	private void guiprintln(String s){
		if(!(gui == null)){
			gui.addlogln(s);
		}
	}
	
}
