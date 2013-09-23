package de.unitrier.cldh.pali.tagger;

import java.util.Iterator;

import de.unitrier.cldh.pali.core.Evaluator;
import de.unitrier.cldh.pali.core.ResourceManager;


public class TrigramTagger implements Tagger {
	/**
	 * @author BobP
	 */
	private ResourceManager resourceManager;
		
	public TrigramTagger(){
		resourceManager = new ResourceManager();
	}
	public TrigramTagger(ResourceManager rm){
		this.resourceManager = rm;
	}
	
	/**
	 * reads a file and takes it as base to learn the tagger
	 * @param fieName file to learn from
	 */
	@Override
	public void train(String fileName) {
		System.out.println("Start training with the file "+fileName);
		resourceManager.addResource(fileName);
		System.out.println("Finished training!");
	}
	
	/**
	 * tags the data in the file fileName
	 * @param fieName file to be tagged
	 */
	@Override
	public void tag(){
		System.out.println("Start tagging...");
		String f_token = "", s_token = "", t_token = "";
		String[] tag_sequence = new String[3];
		
		for(int i = 2; i<resourceManager.getTagData().size();i++){
			f_token = resourceManager.getTagData().get(i);
			s_token = resourceManager.getTagData().get(i-1);
			t_token = resourceManager.getTagData().get(i-2);
			double e = 0, q = 0, max_poss = 0;
			
			//everything what passes this check is a tritagsequence of the same sentence
			if(!(f_token.equals("") || s_token.equals("") || t_token.equals(""))){
				
				Iterator<String> first, second, third;
				
				/**
				 * test for every of this 3 token if it occurs atleast once with a arbitrary tag if so remember it and calc
				 * the probabilty, if its the hightest seen for this token tag token with it
				 */
				first = resourceManager.getPossibleTags().iterator();
				while(first.hasNext()){
					
					tag_sequence[0] = first.next();
					if(resourceManager.getTokenTagFrequency(f_token.toLowerCase(), tag_sequence[0])>0){
						second = resourceManager.getPossibleTags().iterator();
						while(second.hasNext()){
							
							tag_sequence[1] = second.next();
							if(resourceManager.getTokenTagFrequency(s_token.toLowerCase(), tag_sequence[1])>0){
								third = resourceManager.getPossibleTags().iterator();
								while(third.hasNext()){
									
									tag_sequence[2] = third.next();
									if(resourceManager.getTokenTagFrequency(t_token.toLowerCase(), tag_sequence[2])>0){
										//here we know for each of this 3 tokens a possible tag
										//calc our possibility
										e = (	e(f_token.toLowerCase(),tag_sequence[0], resourceManager) 	* 
												e(s_token.toLowerCase(),tag_sequence[1], resourceManager) 	* 
												e(t_token.toLowerCase(),tag_sequence[2], resourceManager)   );
										
										q = q(tag_sequence[0],tag_sequence[1],tag_sequence[2], resourceManager);
										
										//if has a higher prob, change the tags
										if(e*q>max_poss){
											max_poss = e*q;
											
											resourceManager.getTagData().set(i,   f_token+"\t"+tag_sequence[0]);
											resourceManager.getTagData().set(i-1, s_token+"\t"+tag_sequence[1]);
											resourceManager.getTagData().set(i-2, t_token+"\t"+tag_sequence[2]);
											
										}
									}
								}
							}
						}
					}
				}
				
			}
		}
		System.out.println("Finished tagging!");
	}
	
	/**
	 * exports the data into fileName.csv
	 */
	@Override
	public void export(String fileName) {
		
		int ret = resourceManager.export(fileName);
		if(ret==0){
			System.out.println("File "+fileName+".csv has been created!");
		}else{
			System.out.println("ERROR couldnt create the File "+fileName);
		}
		
	}
	
	/**
	 * evalutaes the our tagger and prints it out
	 */
	@Override
	public void evaluate() {
		new Evaluator("test.csv","output.csv");
	}
	
	//=====================================================================private functions
	/**
	 * calcs the probability of seeing the tag s immediately after the bigram of tags (u, v)
	 * @param u tag
	 * @param v tag
	 * @param s tag
	 * @return probabilty
	 */
	private double q(String u, String v, String s, ResourceManager rm){
		double ret = 0;
		
		if(rm.getTritagCount(u+"\t"+v+"\t"+s)!=0){
			ret = ((double)rm.getBitagCount(u+"\t"+v) / (double)rm.getTritagCount(u+"\t"+v+"\t"+s));
		}
		return ret;
	}
	
	/**
	 * calcs the probability of seeing token x paired with tag s
	 * @param x token
	 * @param s tag
	 * @return probability
	 */
	private double e(String x, String s, ResourceManager rm){
		double ret = 0;
		
		if(rm.getUnitagCount(s)!=0){
			ret = ((double)rm.getTokenTagFrequency(x, s)/(double)rm.getUnitagCount(s));
		}
		return ret;
	}
}
