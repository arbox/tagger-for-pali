package de.unitrier.cldh.pali.tagger;

public interface Tagger{
	public void train(String fileName);
	
	public void tag();
	
	public void export(String fileName);
	
	public void evaluate();
}
