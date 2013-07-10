package de.unitrier.cldh.pali.tagger;

public interface Tagger{
	public void train(String fileName);
	
	public void tag(String fileName);
	
	public void export(String fileName);
}
