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
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TaggerGUI implements ActionListener{
	
	private JFrame f;
	private JTextField filetoTag_jtf, filetoTrain_jtf, filetoOP_jtf;
	private JButton openTrain_btn, openTag_btn, openOP_btn, train_btn, tag_btn, save_btn, run_btn ;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;
	private JFileChooser fc;
	private JTextArea log;
	private String prefix;
	private String[] taggerlist;
	private Tagger[] taggers;
	
	public TaggerGUI(){
		Config();
		
		f.setVisible(true);
	}
	
	
	/**
	 * Configurates the frame, its Components, adds them to the Frame itself
	 * and initalizes used Objects
	 */
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	private void Config() {
		// initialize the used objects
		f  = new JFrame();
		fc = new JFileChooser(System.getProperty("user.dir"));
		prefix = "[ElePali] - ";
		taggerlist = new String[2];
		taggers   = new Tagger[2];
		
		// config the Frame itself
		f.setIconImage(new ImageIcon("data/icon.png").getImage()); 
		// ^--the img in icon.png is created by myself so its free to use
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setTitle("ElePali-Tagger");
		f.setResizable(false);
		f.setSize(800,600);
		f.setLocation(100,100);
		
		// add, define & config the Menubar
		setMenubar();
		
		// define & config the combobox
		taggerlist[0] = "Unigram Tagger";
		taggerlist[1] = "Bigram Tagger";
		
		taggers[0] = new UniTagger();
		taggers[1] = new UniTagger();
		
		cb = new JComboBox(taggerlist);
		cb.setSize(150,25);
		cb.setLocation(110,115);
		
		// define & config labels
		JLabel label_cb = new JLabel("Used Tagger");
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
		filetoTrain_jtf.setText(System.getProperty("user.dir")+"\\pali-goldstandard1.csv");
		
		filetoTag_jtf = new JTextField(200);
		filetoTag_jtf.setSize(510,25);
		filetoTag_jtf.setLocation(110,45);
		filetoTag_jtf.setText(System.getProperty("user.dir")+"\\pali-goldstandard2.csv");
		
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
		log.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		log.setBackground(new Color(0x404040));
		log.setForeground(new Color(0xFFFFFF));
		//this.addlogmore("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
		
		// define & config our Menubuttons (Train, Tag, Save, Run)
		train_btn = new JButton("Train");
		train_btn.setSize(80,25);
		train_btn.setLocation(330,115);
		train_btn.addActionListener(this);
		
		tag_btn = new JButton("Tag");
		tag_btn.setSize(80,25);
		tag_btn.setLocation(420,115);
		tag_btn.addActionListener(this);
		
		save_btn = new JButton("Save");
		save_btn.setSize(80,25);
		save_btn.setLocation(510,115);
		save_btn.addActionListener(this);
		
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
		f.add(log);
		
		// force swing into the nulllayoutmanager
		f.setLayout(null);
	}
	
	
	/**
	 * Reacts to clicks on Buttons on our Frame, and executes in respond to the chosen Button
	 * @param e 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == train_btn){
			taggers[cb.getSelectedIndex()].train(filetoTrain_jtf.getText());
		}
		
		if(e.getSource() == tag_btn){
			taggers[cb.getSelectedIndex()].tag(filetoTag_jtf.getText());
		}
		
		if(e.getSource() == save_btn){
			taggers[cb.getSelectedIndex()].export(filetoOP_jtf.getText());
		}
		
		if(e.getSource() == run_btn){
			taggers[cb.getSelectedIndex()].train(filetoTrain_jtf.getText());
			taggers[cb.getSelectedIndex()].tag(filetoTag_jtf.getText());
			taggers[cb.getSelectedIndex()].export(filetoOP_jtf.getText());
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
				log.append(prefix+toAdd+"\n");
				toAdd = "";
			}
		}
	}
	
	public void addlogln(String s){
		log.append(prefix+s+"\n");
	}
	
	
}
