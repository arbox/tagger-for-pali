package de.unitrier.cldh.pali.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PyExe {
	public PyExe(){
		
	}
	
	/**
	 * executes the pythonscript fileName with the arguments in args
	 * @param fileName name of the called script
	 * @param args array with the passed arguments
	 * @return returns a bufferedreader to read the output
	 */
	public BufferedReader  execute(String fileName, String[] args){
		// setup the command and parameter
		String pythonScriptPath = fileName;
		String[] cmd = new String[2+args.length];
		cmd[0] = "python";
		cmd[1] = pythonScriptPath;
		
		// pass the arguments into the cmd to call the pythonscript
		for(int i=0;i<args.length;i++){
			cmd[i+2] = args[i];
		}
		 
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = rt.exec(cmd);
		} catch (IOException e) {
			System.out.println("Couldnt find "+pythonScriptPath);
		}
		 
		// return output from python script
		return new BufferedReader(new InputStreamReader(pr.getInputStream()));
	}
}
