package de.unitrier.cldh.pali.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import de.unitrier.cldh.pali.tagger.Tagger;
import de.unitrier.cldh.pali.tagger.TrigramTagger;
import de.unitrier.cldh.pali.tagger.UniTagger;

public class Launcher {
	private static PyExe pyexe;
	/**
	 * Parses the cmdlineoptions and opens the TaggerGUI which by itself controls the workflow of the program
	 * or runs the chosen tagger in the commandline
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("t", true, "chosen tagger");
		options.addOption("r", true, "all steps at a time");
		options.addOption("gui", false, "opens up the gui");
		
		CommandLineParser parser = new PosixParser();
		
		CommandLine cmd = parser.parse( options, args);
		
		//look if we have to open the gui, if so let it control the workflow
		if(cmd.hasOption("gui")){
			System.out.println("> Opening the GUI");
			new TaggerGUI();
		}else{
			//else look if we have everything we need, do the workflow here
			if(cmd.hasOption("t")){
				pyexe = new PyExe();
				String chosenTagger = cmd.getOptionValue('t');
				System.out.println(chosenTagger);
				chosenTagger = chosenTagger.toLowerCase();
				if(chosenTagger.equals("unigram") || chosenTagger.equals("trigram")){
					if(cmd.hasOption("r")){
						//set called python to a different one, checks if correct in PyExe itself
						if(cmd.hasOption("p")){
							pyexe.setDiffVer(cmd.getOptionValue('p'));
						}
						String relPath = cmd.getOptionValue('r');
						//check atleast if the chosen path ends with .csv
						if(relPath.endsWith(".csv")){
							//everything we need, so run the tagger(note we know already its unigram or trigram)
							Tagger t;
							if(chosenTagger.equals("unigram")){
								System.out.println("> Unigramtagger");
								//it must be unigram
								t = new UniTagger();
								
								//create train,test.csv with splitCorpus.py
								String[] args1 = new String[1];
								args1[0]=relPath;
								
								pyexe.execute("splitCorpus.py", args1);
								//wait half a second, tests showed sometimes java wants to read faster 
								//then os/python could create the files
								Thread.sleep(500);
								
								t.train("train.csv");
								t.tag("test.csv");
								t.export("output.csv");
								t.evaluate();
							}else{
								System.out.println("> Trigramtagger");
								//it must be trigram
								t = new TrigramTagger();
								
								//create train,test.csv with splitCorpus.py
								String[] args1 = new String[1];
								args1[0]=relPath;
								pyexe.execute("splitCorpus.py", args1);
								//wait half a second, tests showed sometimes java wants to read faster 
								//then os/python could create the files
								Thread.sleep(500);
								
								t.train("train.csv");
								t.tag("test.csv");
								t.export("output.csv");
								t.evaluate();
							}
						}
					}else{
						throw new ParseException("");
					}
				}else{
					throw new ParseException("");
				}
			}else{
				throw new ParseException("");
			}
		}
		
		} catch (ParseException | InterruptedException e) {
			System.out.println("Usage: <jarName> [-t TAGGER] [-r RESOURCE] [-p PYVERSION<optional>]		OR		<jarName> [-gui]");
			System.out.println("\n[-t] - tagger taken for the POS-process\n>	valid values: \"unigram\" or \"trigram\"");
			System.out.println("\n[-r] - path to the resource to be tagged\n>	remark: has to be a csv-file & is relative to the jar directory");
			System.out.println("\n[-p] - to call another pythonversion\n>	valid values : \"python\" or \"python3\"");
			//e.printStackTrace();
		}
	}

}
