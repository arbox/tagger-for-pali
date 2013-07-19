package de.unitrier.cldh.pali.tagger;

import de.unitrier.cldh.pali.core.Evaluator;
import de.unitrier.cldh.pali.core.ResourceManager;


public class UniTagger implements Tagger {
	/**
	 * @author BobP
	 */
	private ResourceManager resourceManager;
		
	public UniTagger(){
		resourceManager = new ResourceManager();
	}
	public UniTagger(ResourceManager rm){
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
		String token = "";
		for(int i = 0; i<resourceManager.getTagData().size();i++){
			token = resourceManager.getTagData().get(i);
			//first check is to determine if its an empty line, second check is to determine if the line is already tagged
			//if so just dont tag it
			if(!token.equals("") && token.indexOf("\t")==-1){
				resourceManager.getTagData().set(i, token+"\t"+resourceManager.getMaxTagByFrequencySeen(token.toLowerCase()));
			}
		}

		//resourceManager.printTagData();
		System.out.println("Finished tagging!");
	}
	
	/**
	 * this method is to tag already read in data, so it needs an ResourceManager to do so
	 * @param rm
	 */
	public ResourceManager tag(ResourceManager rm){
		String token = "";
		for(int i = 0; i<rm.getTagData().size();i++){
			token = rm.getTagData().get(i);
			//first check is to determine if its an empty line, second check is to determine if the line is already tagged
			//if so just dont tag it
			if(!token.equals("") && token.indexOf("\t")==-1){
				rm.getTagData().set(i, token+"\t"+resourceManager.getMaxTagByFrequencySeen(token.toLowerCase()));
			}
		}
		//resourceManager.printTagData();
		return rm;
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
}
