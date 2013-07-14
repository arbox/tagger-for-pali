package de.unitrier.cldh.pali.core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.unitrier.cldh.pali.tagger.Tagger;
import de.unitrier.cldh.pali.tagger.TrigramTagger;
import de.unitrier.cldh.pali.tagger.UniTagger;

public class TaggerGUI implements ActionListener{
	
	private JFrame f;
	private JTextField second_jtf, first_jtf, filetoOP_jtf;
	private JButton openTrain_btn, openTag_btn, openOP_btn, train_btn, tag_btn, save_btn, eval_btn, run_btn ;
	private JComboBox<String> cb;
	private JFileChooser fc;
	private JTextArea log;
	private JScrollPane scroll;
	private String sep;
	private String[] taggerlist;
	private Tagger[] taggers;
	private PyExe pyexe;
	public TaggerGUI(){
		Config();
		
		f.setVisible(true);
	}
	
	
	/**
	 * Configurates the frame, its Components, adds them to the Frame itself
	 * and initalizes used Objects
	 */
	private void Config() {
		// initialize the used objects
		f  = new JFrame();
		fc = new JFileChooser(System.getProperty("user.dir"));
		taggerlist = new String[2];
		taggers   = new Tagger[2];
		pyexe = new PyExe();
		
		sep = "";  // v--Switches the seperator used between directories in addresses dependend on the OS
		if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0){
			sep = "\\";  //Windows
		}else{
			sep = "/";   //Unix
		}
		
		// config the Frame itself
		f.setIconImage(new ImageIcon("../data/icon.png").getImage());
		// ^--the img in icon.png is created by myself so its free to use
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("ElePali-Tagger");
		f.setResizable(false);
		f.setSize(800,400);
		f.setLocation(100,100);
		
		// add, define & config the Menubar
		setMenubar();
		
		// define & config the combobox
		taggerlist[0] = "Unigram Tagger";
		taggerlist[1] = "HMM Trigram Tagger";
		
		//taggers[0] = new UniTagger(this);
		taggers[0] = new UniTagger(this);
		taggers[1] = new TrigramTagger(this);
		
		cb = new JComboBox<String>(taggerlist);
		cb.setSize(150,25);
		cb.setLocation(110,80);
		cb.setSelectedIndex(1);
		
		// define & config labels
		JLabel label_cb = new JLabel("Used Tagger");
		label_cb.setSize(150,25);
		label_cb.setLocation(20,80);
		
		JLabel label_tr = new JLabel("File to train");
		label_tr.setSize(150,25);
		label_tr.setLocation(20,10);
		
		JLabel label_tg = new JLabel("File to export");
		label_tg.setSize(150,25);
		label_tg.setLocation(20,45);
		
		JLabel label_ex = new JLabel("File to export2");
		label_ex.setSize(150,25);
		label_ex.setLocation(20,80);
		
		// define & config inputfields
		first_jtf = new JTextField(200);
		first_jtf.setSize(510,25);
		first_jtf.setLocation(110,10);
		first_jtf.setText(System.getProperty("user.dir")+sep+"inputwos.csv");
		
		second_jtf = new JTextField(200);
		second_jtf.setSize(510,25);
		second_jtf.setLocation(110,45);
		second_jtf.setText(System.getProperty("user.dir")+sep+"output.csv");
		
		filetoOP_jtf = new JTextField(200);
		filetoOP_jtf.setSize(510,25);
		filetoOP_jtf.setLocation(110,80);
		filetoOP_jtf.setText(System.getProperty("user.dir")+sep+".."+sep+"output"+sep+"output.csv");
		
		// define & config buttons
		openTrain_btn = new JButton("Choose...");
		openTrain_btn.setSize(150,25);
		openTrain_btn.setLocation(630,10);
		openTrain_btn.addActionListener(this);
		 
		openTag_btn = new JButton("Choose...");
		openTag_btn.setSize(150,25);
		openTag_btn.setLocation(630,45);
		openTag_btn.addActionListener(this);
		
		openOP_btn = new JButton("Choose...");
		openOP_btn.setSize(150,25);
		openOP_btn.setLocation(630,80);
		openOP_btn.addActionListener(this);
		
		// define & config the log
		log = new JTextArea();
		log.setSize(756,220);
		log.setLocation(20,115);
		log.setEditable(false);
		log.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		log.setBackground(new Color(0x404040));
		log.setForeground(new Color(0xFFFFFF));
		
		// add a scrollbar to the log
		scroll = new JScrollPane (log);
		scroll.setBounds(20, 150, 756, 390);
		scroll.setSize(756,220);
		scroll.setLocation(20,115);
		
		// define & config our Menubuttons (Train, Tag, Export, Run)
		train_btn = new JButton("Train");
		train_btn.setSize(70,25);
		train_btn.setLocation(280,80);//115
		train_btn.addActionListener(this);
		
		tag_btn = new JButton("Tag");
		tag_btn.setSize(70,25);
		tag_btn.setLocation(360,80);
		tag_btn.addActionListener(this);
		
		save_btn = new JButton("Export");
		save_btn.setSize(80,25);
		save_btn.setLocation(440,80);
		save_btn.addActionListener(this);
		
		eval_btn = new JButton("Evaluate");
		eval_btn.setSize(80,25);
		eval_btn.setLocation(530,80);
		eval_btn.addActionListener(this);
		
		run_btn = new JButton("Run");
		run_btn.setSize(160,25);
		run_btn.setLocation(620,80);
		run_btn.addActionListener(this);
		
		// add the components to the frame
		f.add(first_jtf);
		f.add(second_jtf);
		//f.add(filetoOP_jtf);
		f.add(openTrain_btn);
		f.add(openTag_btn);
		//f.add(openOP_btn);
		f.add(train_btn);
		f.add(tag_btn);
		f.add(save_btn);
		f.add(eval_btn);
		f.add(run_btn);
		f.add(cb);
		f.add(label_cb);
		f.add(label_tr);
		f.add(label_tg);
		//f.add(label_ex);
		//f.add(log);
		f.add(scroll);
		// force swing into the nulllayoutmanager
		f.setLayout(null);
	}
	
	
	/**
	 * Reacts to clicks on Buttons on our Frame, and executes in respond to the chosen Button
	 * @param e 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
		if(e.getSource() == train_btn){
			log.setText("");
			//create train,test.csv with splitCorpus.py
			splitCorp();
			taggers[cb.getSelectedIndex()].train("train.csv");
		}
		
		if(e.getSource() == tag_btn){
			log.setText("");
			taggers[cb.getSelectedIndex()].tag("test.csv");
		}
		
		if(e.getSource() == save_btn){
			taggers[cb.getSelectedIndex()].export("output.csv");
		}
		
		if(e.getSource() == eval_btn){
			taggers[cb.getSelectedIndex()].evaluate();
		}
		
		if(e.getSource() == run_btn){
			//create train,test.csv with splitCorpus.py
			splitCorp();
			
			taggers[cb.getSelectedIndex()].train("train.csv");
			taggers[cb.getSelectedIndex()].tag("test.csv");
			taggers[cb.getSelectedIndex()].export("output.csv");
			taggers[cb.getSelectedIndex()].evaluate();
		}
		
		if(e.getSource() == openTrain_btn){
			// show Dialog to select a file
	        int ret = fc.showOpenDialog(null);
	        // proofs if open was choosed
	        if(ret == JFileChooser.APPROVE_OPTION){
	        	first_jtf.setText(fc.getSelectedFile().getAbsolutePath());
	        }
		}
		
		if(e.getSource() == openTag_btn){
			// show Dialog to select a file
	        int ret = fc.showOpenDialog(null);
	        // proofs if open was choosed
	        if(ret == JFileChooser.APPROVE_OPTION){
	        	second_jtf.setText(fc.getSelectedFile().getAbsolutePath());
	        }
		}
		
		if(e.getSource() == openOP_btn){
			// show Dialog to select a file
	        int ret = fc.showOpenDialog(null);
	        // proofs if open was choosed
	        if(ret == JFileChooser.APPROVE_OPTION){
	        	filetoOP_jtf.setText(fc.getSelectedFile().getAbsolutePath());
	        }
		}
		
		} catch (InterruptedException er) {
			//should never happen unless the os rly has a bad day
			er.printStackTrace();
		}
	}
	
	/**
	 * Generates a Menubar used in the upper Area of the Frame
	 */
	private void setMenubar(){
		JMenu file = new JMenu("File");
		file.setMnemonic('F');
		
		JMenuItem trainfile = new JMenuItem("Choose a Trainfile...");
		file.add(trainfile);
		
		JMenuItem tagfile = new JMenuItem("Choose a Tagfile...");
		file.add(tagfile);	
		
		JMenuItem exitItem = new JMenuItem("Exit");
		file.add(exitItem);
		
		//adding action listener to menu items
		trainfile.addActionListener(
			new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// show Dialog to select a file
			        int ret = fc.showOpenDialog(null);
			        // proofs if open was choosed
			        if(ret == JFileChooser.APPROVE_OPTION){
			        	first_jtf.setText(fc.getSelectedFile().getAbsolutePath());
			        }
					
				}
			}
		);
		tagfile.addActionListener(
			new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// show Dialog to select a file
			        int ret = fc.showOpenDialog(null);
			        // proofs if open was choosed
			        if(ret == JFileChooser.APPROVE_OPTION){
			        	second_jtf.setText(fc.getSelectedFile().getAbsolutePath());
			        }
				}
				
			}
		);
		
		exitItem.addActionListener(
			new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					System.out.println("Exit is pressed");
					f.dispose();
				}
			}
		);						
		JMenuBar bar = new JMenuBar();
		f.setJMenuBar(bar);
		bar.add(file);
		
		f.getContentPane();
	}
	/**
	 * Adds the String s to the log and also newlinefeeds after roughly 110 chars
	 * @param s String added to the log
	 */
	public void addlogmore(String s){
		String toAdd = "";
		StringTokenizer st = new StringTokenizer(s," ");
		while(st.hasMoreTokens()){
			toAdd = toAdd + " " + st.nextToken();
			if((toAdd.length())>110){
				log.append(toAdd+"\n");
				toAdd = "";
			}
		}
	}
	
	/**
	 * Adds the String s to the log with a newlinefeed afterwards
	 * @param s String added to the log
	 */
	public void println(String s){
		if(s!=null){
			log.append(s+"\n");
			
			// set the Scrollbar to bottom
			scroll.getVerticalScrollBar().setValue( scroll.getVerticalScrollBar().getMaximum() );
		
		}
	}
	
	private void splitCorp() throws InterruptedException{
		//create train,test.csv with splitCorpus.py
		String[] args = new String[1];
		args[0]=first_jtf.getText();
		pyexe.execute("splitCorpus.py", args);
		//wait half a second, tests showed sometimes java wants to read faster 
		//then os/python could create the files
		Thread.sleep(500);
	}
}
