package de.unitrier.cldh.pali.core;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import de.unitrier.cldh.pali.tagger.BigramTagger;
import de.unitrier.cldh.pali.tagger.Tagger;
import de.unitrier.cldh.pali.tagger.TrigramTagger;
import de.unitrier.cldh.pali.tagger.UniTagger;

public class Launcher {
	private static String version = "1.2";
	/**
	 * Parses the cmdlineoptions and opens the TaggerGUI which by itself controls the workflow of the program
	 * or runs the chosen tagger in the commandline
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Options options = new Options();

			//options for the POSTagger
			options.addOption("tr", true, "");
			options.addOption("tg", true, "");
			options.addOption("s", true, "");
			options.addOption("u", false, "");
			//options for the evaluator
			options.addOption("e", false, "");
			options.addOption("g",true, "");
			options.addOption("o",true,"");

			CommandLineParser parser = new PosixParser();

			CommandLine cmd = parser.parse( options, args);

			//user wants a gui?
			if(cmd.hasOption("u")){
				new TaggerGUI("Caterpillar v."+version);
			}else{
				//every argument we want is there?
				if(cmd.hasOption("tr") && cmd.hasOption("tg") && cmd.hasOption("s")){
					if( //ends with csv
						cmd.getOptionValue("tr").endsWith(".csv") && cmd.getOptionValue("tg").endsWith(".csv") &&
						//strategy is a valid one
						(cmd.getOptionValue("s").equals("s1") || cmd.getOptionValue("s").equals("s2") || 
						 cmd.getOptionValue("s").equals("s3") || cmd.getOptionValue("s").equals("s4") || cmd.getOptionValue("s").equals("s5")) 
					){//here we know everything we need is there, and atleast the strategy is well known
						
						ResourceManager rm = new ResourceManager();
						rm.addResource(cmd.getOptionValue("tr"));
						rm.setTagData(cmd.getOptionValue("tg"));
						
						int strategy = Integer.parseInt(String.valueOf(cmd.getOptionValue("s").charAt(cmd.getOptionValue("s").length()-1)));
						switch(strategy){
						case 1:
							//[tested]
							System.out.println("TrigramTagger > BigramTagger > UnigramTagger > DONE");
							
							Tagger[] t1 = new Tagger[3];
							//Step 1:TrigramTagger
							t1[0] = new TrigramTagger(rm);
							t1[0].tag();
							
							//Step 2:BigramTagger
							t1[1] = new BigramTagger(rm);
							t1[1].tag();
							
							//Step 3:UnigramTagger
							t1[2] = new UniTagger(rm);
							t1[2].tag();
							
							break;
						case 2:
							//[tested]
							System.out.println("BigramTagger > UnigramTagger > DONE");
							
							Tagger[] t2 = new Tagger[2];
							//Step 1:BigramTagger
							t2[0] = new BigramTagger(rm);
							t2[0].tag();
							
							//Step 2:UniTagger
							t2[1] = new UniTagger(rm);
							t2[1].tag();
							
							break;
						case 3:
							//[tested]
							System.out.println("UnigramTagger > DONE");
							
							Tagger[] t3 = new Tagger[1];
							t3[0] = new UniTagger(rm);
							t3[0].tag();
							
							break;
						case 4:
							//[tested]
							System.out.println("BigramTagger > DONE");
							
							Tagger[] t4 = new Tagger[1];
							t4[0] = new BigramTagger(rm);
							t4[0].tag();
							
							break;
						case 5:
							//[tested]
							System.out.println("TrigramTagger > DONE");
							
							Tagger[] t5 = new Tagger[1];
							t5[0] = new TrigramTagger(rm);
							t5[0].tag();
							
							break;
						}
						
						rm.export("output.csv");
						new Evaluator("test.csv","output.csv");
					} else {
						throw new ParseException("");
					}
					
				} else {
					if (cmd.hasOption("e") && cmd.hasOption("g") && cmd.hasOption("o") && 
							cmd.getOptionValue("g").endsWith(".csv") && cmd.getOptionValue("o").endsWith(".csv")){
						new Evaluator(cmd.getOptionValue("g"), cmd.getOptionValue("o"));
					} else {
						throw new ParseException("");
					}
				}
			}
		} catch (ParseException e) {
			System.out.println("Caterpillar Version " + version + "\nUsage:\n" + String.format("%11s","") + "<jarName> [-tr arg] [-tg arg] [-s arg]");
			System.out.println(String.format("%10s","OR")+" <jarName> [-g arg] [-o arg] -e");
			System.out.println(String.format("%10s","OR")+" <jarName> -u\n");
			System.out.println("Caterpillar is a POS-Tagger for the Middle Indo-Aryan language Pali. It also contains an Evaluator");
			System.out.println("to measure the Resulsts delivered by the Tagger which can be called as in the second Example in Usage.");
			System.out.println("The Application supports 3 Types of HMM-Model Taggers (Unigram, Bigram & Trigram) and provides different");
			System.out.println("strategies to fall back at in the Case a Tagger couldn't tag some Data.\n");
			
			System.out.println("Details to the Options:\n");
			System.out.println("\tOptions for the POS-Tagger:\n");
			System.out.println(String.format("%25s\t", "-tr, --train") + "Opens a graphical user Interface.\n");
			System.out.println(String.format("%25s\t", "-tr, --train") + "Specifies the File taken to Train the Taggers.\n");
			System.out.println(String.format("%25s\t", "-tg, --tag") + "Specifies the File taken to be tagged by the Taggers.\n");
			System.out.println(String.format("%25s\t", "-s, --strategy") + "Strategy the Tagger will take to fall back at.");
			System.out.println(String.format("%25s\t","") + "Valid values:\n");
			System.out.println(String.format("%25s\t","") + "\"s1\" - TrigramTagger > BigramTagger > UnigramTagger > DONE\n");
			System.out.println(String.format("%25s\t","") + "\"s2\" - BigramTagger > UnigramTagger > DONE\n");
			System.out.println(String.format("%25s\t","") + "\"s3\" - UnigramTagger > DONE\n");
			System.out.println(String.format("%25s\t","") + "\"s4\" - BigramTagger > DONE\n");
			System.out.println(String.format("%25s\t","") + "\"s5\" - TrigramTagger > DONE\n");
			System.out.println("\tOptions for the Evaluator:\n");
			System.out.println(String.format("%25s\t", "-e, --evaluator") + "Switches the Application into the Evaluationmode.\n");
			System.out.println(String.format("%25s\t", "-g, --goldstandard") + "Specifies the Goldstandardfile taken to compare with the Taggeroutput.\n");
			System.out.println(String.format("%25s\t", "-o, --output") + "Specifies the Taggeroutputfile taken to compare with the Goldstandard.\n");
			//e.printStackTrace();
		}
	}

}
