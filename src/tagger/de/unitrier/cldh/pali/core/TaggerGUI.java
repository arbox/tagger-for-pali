package de.unitrier.cldh.pali.core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
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

import de.unitrier.cldh.pali.tagger.BigramTagger;
import de.unitrier.cldh.pali.tagger.Tagger;
import de.unitrier.cldh.pali.tagger.TrigramTagger;
import de.unitrier.cldh.pali.tagger.UniTagger;


public class TaggerGUI implements ActionListener{
	
	private JFrame f;
	private JTextField filetoTag_jtf, filetoTrain_jtf, filetoOP_jtf;
	private JButton openTrain_btn, openTag_btn, openOP_btn, train_btn, tag_btn, save_btn, run_btn ;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;
	private JFileChooser fc;
	private JTextArea log;
	private JScrollPane scroll;
	private String[] strategylist;
	private ResourceManager rm;
	public TaggerGUI(String title){
		Config(title);
		
		f.setVisible(true);
	}
	
	
	/**
	 * Configurates the frame, its Components, adds them to the Frame itself
	 * and initalizes used Objects
	 */
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	private void Config(String title) {
		// initialize the used objects
		f  = new JFrame();
		fc = new JFileChooser(System.getProperty("user.dir"));
		strategylist = new String[5];
		
		// config the Frame itself
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setTitle(title);
		f.setResizable(false);
		f.setSize(800,600);
		f.setLocation(100,100);
		
		// add, define & config the Menubar
		setMenubar();
		
		// define & config the combobox
		strategylist[0] = "Tri>Bi>Uni>Done";
		strategylist[1] = "Bi>Uni>Done";
		strategylist[2] = "Uni>Done";
		strategylist[3] = "Bi>Done";
		strategylist[4] = "Tri>Done";
		
		cb = new JComboBox(strategylist);
		cb.setSize(150,25);
		cb.setLocation(110,115);
		
		// define & config labels
		JLabel label_cb = new JLabel("Used Strategy");
		label_cb.setSize(150,25);
		label_cb.setLocation(20,115);
		
		JLabel label_tr = new JLabel("File to train");
		label_tr.setSize(150,25);
		label_tr.setLocation(20,10);
		
		JLabel label_tg = new JLabel("File to tag");
		label_tg.setSize(150,25);
		label_tg.setLocation(20,45);
		
		JLabel label_ex = new JLabel("File to export");
		label_ex.setSize(150,25);
		label_ex.setLocation(20,80);
		
		// define & config inputfields
		filetoTrain_jtf = new JTextField(200);
		filetoTrain_jtf.setSize(510,25);
		filetoTrain_jtf.setLocation(110,10);
		filetoTrain_jtf.setText(System.getProperty("user.dir")+"\\train.csv");
		
		filetoTag_jtf = new JTextField(200);
		filetoTag_jtf.setSize(510,25);
		filetoTag_jtf.setLocation(110,45);
		filetoTag_jtf.setText(System.getProperty("user.dir")+"\\test.csv");
		
		filetoOP_jtf = new JTextField(200);
		filetoOP_jtf.setSize(510,25);
		filetoOP_jtf.setLocation(110,80);
		filetoOP_jtf.setText(System.getProperty("user.dir")+"\\output.csv");
		
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
		log.setSize(756,390);
		log.setLocation(20,150);
		log.setEditable(false);
		log.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		log.setBackground(new Color(0x404040));
		log.setForeground(new Color(0xFFFFFF));
		
		// add a scrollbar to the log
		scroll = new JScrollPane (log);
		scroll.setBounds(20, 150, 756, 390);
		scroll.setSize(756,390);
		scroll.setLocation(20,150);
		
		// define & config our Menubuttons (Train, Tag, Export, Run)
		train_btn = new JButton("Train");
		train_btn.setSize(80,25);
		train_btn.setLocation(330,115);
		train_btn.addActionListener(this);
		
		tag_btn = new JButton("Tag");
		tag_btn.setSize(80,25);
		tag_btn.setLocation(420,115);
		tag_btn.addActionListener(this);
		tag_btn.setEnabled(false);
		
		save_btn = new JButton("Export");
		save_btn.setSize(80,25);
		save_btn.setLocation(510,115);
		save_btn.addActionListener(this);
		save_btn.setEnabled(false);
		
		run_btn = new JButton("Run");
		run_btn.setSize(180,25);
		run_btn.setLocation(600,115);
		run_btn.addActionListener(this);
		
		// add the components to the frame
		f.add(filetoTrain_jtf);
		f.add(filetoTag_jtf);
		f.add(filetoOP_jtf);
		f.add(openTrain_btn);
		f.add(openTag_btn);
		f.add(openOP_btn);
		f.add(train_btn);
		f.add(tag_btn);
		f.add(save_btn);
		f.add(run_btn);
		f.add(cb);
		f.add(label_cb);
		f.add(label_tr);
		f.add(label_tg);
		f.add(label_ex);
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
		/*rm = new ResourceManager();
		rm.addResource(cmd.getOptionValue("tr"));
		rm.setTagData(cmd.getOptionValue("tg"));*/
		if(e.getSource() == train_btn){
			addlogln("Starting to train with:\n"+filetoTrain_jtf.getText());
			rm = new ResourceManager();
			rm.addResource(filetoTrain_jtf.getText());
			addlogln("Training ended!");
			tag_btn.setEnabled(true);
		}
		
		if(e.getSource() == tag_btn){
			addlogln("Starting to tag:\n"+filetoTag_jtf.getText()+"\nUsing Strategy - "+strategylist[cb.getSelectedIndex()]);
			rm.setTagData(filetoTag_jtf.getText());
			if(cb.getSelectedIndex()==0){
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
			}
			if(cb.getSelectedIndex()==1){
				Tagger[] t2 = new Tagger[2];
				//Step 1:BigramTagger
				t2[0] = new BigramTagger(rm);
				t2[0].tag();
				
				//Step 2:UniTagger
				t2[1] = new UniTagger(rm);
				t2[1].tag();
			}
			if(cb.getSelectedIndex()==2){
				Tagger[] t3 = new Tagger[1];
				t3[0] = new UniTagger(rm);
				t3[0].tag();
			}
			if(cb.getSelectedIndex()==3){
				Tagger[] t4 = new Tagger[1];
				t4[0] = new BigramTagger(rm);
				t4[0].tag();
			}
			if(cb.getSelectedIndex()==4){
				Tagger[] t5 = new Tagger[1];
				t5[0] = new TrigramTagger(rm);
				t5[0].tag();
			}
			addlogln("Tagging ended!");
			save_btn.setEnabled(true);
		}
		if(e.getSource() == save_btn){
			addlogln("Exporting to:\n"+filetoOP_jtf.getText());
			rm.export(filetoOP_jtf.getText());
			addlogln("File exported!");
		}
		
		if(e.getSource() == run_btn){
			//code from train
			addlogln("Starting to train with:\n"+filetoTrain_jtf.getText());
			rm = new ResourceManager();
			rm.addResource(filetoTrain_jtf.getText());
			addlogln("Training ended!");
			tag_btn.setEnabled(true);
			
			
			//code from tag
			addlogln("Starting to tag:\n"+filetoTag_jtf.getText()+"\nUsing Strategy - "+strategylist[cb.getSelectedIndex()]);
			rm.setTagData(filetoTag_jtf.getText());
			if(cb.getSelectedIndex()==0){
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
			}
			if(cb.getSelectedIndex()==1){
				Tagger[] t2 = new Tagger[2];
				//Step 1:BigramTagger
				t2[0] = new BigramTagger(rm);
				t2[0].tag();
				
				//Step 2:UniTagger
				t2[1] = new UniTagger(rm);
				t2[1].tag();
			}
			if(cb.getSelectedIndex()==2){
				Tagger[] t3 = new Tagger[1];
				t3[0] = new UniTagger(rm);
				t3[0].tag();
			}
			if(cb.getSelectedIndex()==3){
				Tagger[] t4 = new Tagger[1];
				t4[0] = new BigramTagger(rm);
				t4[0].tag();
			}
			if(cb.getSelectedIndex()==4){
				Tagger[] t5 = new Tagger[1];
				t5[0] = new TrigramTagger(rm);
				t5[0].tag();
			}
			addlogln("Tagging ended!");
			save_btn.setEnabled(true);
			
			// code from export
			addlogln("Exporting to:\n"+filetoOP_jtf.getText());
			rm.export(filetoOP_jtf.getText());
			addlogln("File exported!");
		}
		
		if(e.getSource() == openTrain_btn){
			// show Dialog to select a file
	        int ret = fc.showOpenDialog(null);
	        // proofs if open was choosed
	        if(ret == JFileChooser.APPROVE_OPTION){
	        	filetoTrain_jtf.setText(fc.getSelectedFile().getAbsolutePath());
	        }
		}
		
		if(e.getSource() == openTag_btn){
			// show Dialog to select a file
	        int ret = fc.showOpenDialog(null);
	        // proofs if open was choosed
	        if(ret == JFileChooser.APPROVE_OPTION){
	        	filetoTag_jtf.setText(fc.getSelectedFile().getAbsolutePath());
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
		
		JMenuItem opfile = new JMenuItem("Choose a Outputfile...");
		file.add(opfile);	
		
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
			        	filetoTrain_jtf.setText(fc.getSelectedFile().getAbsolutePath());
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
			        	filetoTag_jtf.setText(fc.getSelectedFile().getAbsolutePath());
			        }
				}
				
			}
		);
		
		opfile.addActionListener(
				new ActionListener(){
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// show Dialog to select a file
				        int ret = fc.showOpenDialog(null);
				        // proofs if open was choosed
				        if(ret == JFileChooser.APPROVE_OPTION){
				        	filetoOP_jtf.setText(fc.getSelectedFile().getAbsolutePath());
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
	 * Adds the String s to leg and also newlinefeeds after roughly 110 chars
	 * @param s String which gets added to the log
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
	
	public void addlogln(String s){
		log.append(s+"\n");
		
		// set the Scrollbar to bottom
		scroll.getVerticalScrollBar().setValue( scroll.getVerticalScrollBar().getMaximum() );


	}
	
	
}
