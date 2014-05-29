package compactController;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.java.games.input.ControllerEnvironment;

/* *******************************
 * ******** TODO_LIST!!! *********
 * *******************************
 * 
 * scegliere la periferica da GUI (ma settare come predefinita la ped)
 * 
 * dopo il refresh delle combobox, selezionare se possibile la scelta attuale
 * 
 * +++ caricare il file di configurazione all'avvio (se presente)
 * +++ utilizzare un file di configurazione 
 * 
 * aggiungere l'opzione per il dump nelle preferenze
 * 
 * permettere il learn dei pulsanti da GUI (cambiando poi l'array bName)
 * 
 * *******************************
 * *******************************
 */

public class ControlGui {

	private final int B_NUMBER = 6;
	private final int WIDTH = 380;
	private final int HEIGHT = 240; // + 30;
	
	private PedalController2 workerThread;

	private JFrame app;
	
	/*
	 * Option Container 
	 */
	private JPanel optionContainer;
	
	private JLabel deviceLabel;
	private JComboBox deviceCombo;
	private JButton deviceRefreshButton;
	private JLabel deviceStatus;
	private String actualDevice;
	
	private JLabel midiLabel;
	private JComboBox midiCombo;
	private JLabel midiStatus;
	private JButton midiRefreshButton;
	private String actualMidi;
	
	private JButton cancel_button;
	private JButton apply_button;
	private JButton ok_button;
	
	/*
	 * Main Container
	 */
	private JPanel mainContainer;
	
	private JToggleButton[] button;
	private Rectangle[] buttonLocation;
	
	private JToggleButton[] toggle;
	private Rectangle[] toggleLocation;
	
	private JCheckBox[] reset;
	private Rectangle[] resetLocation;
	
	private JButton opt_button;
	private JMenuItem refreshResetMask;
	private boolean[] resetMask;
	
	
	private void init() {
		initLocations();
		initMainContainer();
		initOptionContainer();
	}
	
	private void initLocations() {
		
		// posizioni bottoni
		buttonLocation = new Rectangle[] {	
			new Rectangle(35, 125, 36, 36),
			new Rectangle(170, 125, 36, 36),
			new Rectangle(305, 125, 36, 36),
			new Rectangle(35, 20, 36, 36),
			new Rectangle(170, 20, 36, 36),
			new Rectangle(305, 20, 36, 36)
		};
		
		// posizioni bottoni retro
		toggleLocation = new Rectangle[] {
			new Rectangle(23, 113, 60, 60),
			new Rectangle(157, 113, 60, 60),
			new Rectangle(292, 113, 60, 60),
			new Rectangle(23, 7, 60, 60),
			new Rectangle(157, 7, 60, 60),
			new Rectangle(292, 7, 60, 60)			
		};
		
		// posizioni checkbox
		resetLocation = new Rectangle[] {
			new Rectangle(24, 180, 76, 16),
			new Rectangle(159, 180, 76, 16),
			new Rectangle(294, 180, 76, 16),
			new Rectangle(24, 75, 76, 16),
			new Rectangle(159, 75, 76, 16),
			new Rectangle(294, 75, 76, 16)
		};	
		
	}
	
	private void setIconActive(JLabel label, boolean active) {
		if(active)
			label.setIcon(new ImageIcon(getClass().getResource("/resources/status_on_18.png")));
		else
			label.setIcon(new ImageIcon(getClass().getResource("/resources/status_off_18.png")));
	}
	
	
	private boolean load() {
		File conf = new File("config.conf");
		if( !conf.exists() || !conf.canRead())
			return false;
		
		String midi=null ,device=null;
		String[] mask = null;
		
		try {
			String line = null;
			
			BufferedReader in = new BufferedReader(new FileReader(conf));
			while( (line = in.readLine()) != null ) {
				
				String value = line.substring( line.indexOf('=') + 1 );
				
				if(line.startsWith("MIDI=")){
					midi = value;
					
				}
				else if(line.startsWith("DEVICE=")){
					device = value;
				}
				else if(line.startsWith("BUTTON_MASK=")){
					if(value != null && value != "") {
						mask = new String[6];
						//inizio parsing
						StringTokenizer tokenizer = new StringTokenizer(value,",");
						if(tokenizer.countTokens() == 6) {
							for(int i=0 ; i<6 ; i++)
								mask[i] = tokenizer.nextToken();							
						}					
					
					}
				}
			}
/* ****************************************** */		
			/* DEBUG_PRINT 
			 */
			if(midi != null) System.out.print("midi: "+ midi +"\n");
			if(device != null) System.out.print("device: "+ device +"\n");
			if(mask != null) for(int i=0;i<mask.length;i++) System.out.print(":"+mask[i]+":");
			/* 
			 */
			
			// MIDI
			if(midi != null){
				midiCombo.setSelectedItem(midi);
				if(workerThread.setMidiOut(midi))
					actualMidi = midi;
			}
			
			// DEVICE
			if(device != null){
				deviceCombo.setSelectedItem(device);
				//manca la parte di sostituzione
			}
			
			// MASK
			if(mask != null){
				///////////////////////////////
			}
			
/* ****************************************** */			
			
			
			
			return true;

		} catch (IOException e) {
			return false;
		}
		
	}
	
	private boolean dump() {
		
		String dmp = "";
		String[] mask = workerThread.getButtonMask();
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter("config.conf"));
		} catch (IOException e) {
			return false;
		}
		
		dmp +="MIDI="+ actualMidi +"\n";

		dmp +="DEVICE="+ actualDevice +"\n";

		dmp +="BUTTON_MASK=";
		for(int i = 0; i<mask.length-1 ; i++)
			dmp += mask[i]+",";
		dmp += mask[mask.length-1] + "\n";
		
		try {
			out.write(dmp);
			out.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;		
	}
	
	private void initOptionContainer() {

		optionContainer = new JPanel();
		optionContainer.setLayout(null);
		
		/* Usb Device */
//		addOptionFieldBlock(new Point(75,15), "Controller in use: ", new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				deviceComboRefresh();				
//			}
//		});
//		deviceComboRefresh();
		
		Point vertice = new Point(75,15); // 75,15
		String label = "Controller in use: ";
		ActionListener dropdownCallback = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deviceComboRefresh();				
			}
		};
		
		deviceRefreshButton = new JButton();
		deviceRefreshButton.setBounds(new Rectangle(((int)vertice.getX()-20),((int)vertice.getY()+25),13,13)); // -20,25
/////////////////////////		
		deviceRefreshButton.setEnabled(false);
/////////////////////////
		deviceRefreshButton.setIcon(new ImageIcon(getClass().getResource("/resources/refresh.png")));
		deviceRefreshButton.addActionListener(dropdownCallback);

		deviceStatus = new JLabel(new ImageIcon(getClass().getResource("/resources/status_off_18.png")));
		deviceStatus.setBounds(new Rectangle(((int)vertice.getX()+235),((int)vertice.getY()+20),18,18)); // 235,20
		
		deviceLabel = new JLabel(label);
		deviceLabel.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()), 270, 16)); // 0,0
		deviceCombo = new JComboBox();
		deviceCombo.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()+20), 226, 22)); // 0,20
		deviceComboRefresh();
		
		/* Midi Out */
//		addOptionFieldBlock(new Point(75,90), "Send midi to: ", new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				midiComboRefresh();				
//			}
//		});	
//		midiComboRefresh();

		vertice = new Point(75,90); // 75, 90
		label = "Send midi to: ";
		dropdownCallback = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				midiComboRefresh();				
			}
		};

		midiRefreshButton = new JButton();
		midiRefreshButton.setBounds(new Rectangle(((int)vertice.getX()-20),((int)vertice.getY()+25),13,13));
		midiRefreshButton.setIcon(new ImageIcon(getClass().getResource("/resources/refresh.png")));
		midiRefreshButton.addActionListener(dropdownCallback);	
		
		midiStatus = new JLabel(new ImageIcon(getClass().getResource("/resources/status_off_18.png")));
		midiStatus.setBounds(new Rectangle(((int)vertice.getX()+235),((int)vertice.getY()+20),18,18));

		midiLabel = new JLabel(label);
		midiLabel.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()), 270, 16));

		midiCombo = new JComboBox();
		midiCombo.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()+20), 226, 22));
		midiComboRefresh(); // primo controllo
		
		
		/*
		 * Buttons
		 */
		cancel_button = new JButton("Cancel");
		cancel_button.setBounds(new Rectangle(74, 165, 70, 31));
		cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelChange(app, mainContainer);
			}
		});
		
		apply_button = new JButton("Apply");
		apply_button.setBounds(new Rectangle(154, 165, 70, 31));
		apply_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				/* ************** INPUT PART ********************/
				actualDevice = (String)deviceCombo.getSelectedItem();
				setIconActive(deviceStatus, true);
				
				/* ************** MIDI PART *********************/
				actualMidi = (String)midiCombo.getSelectedItem();
				// se riesce ad abilitare la nuova accende il led altrimenti lo spegne
				setIconActive(midiStatus, workerThread.setMidiOut(actualMidi) );
				midiLabel.setText("Send midi to: "+actualMidi);
				
				

			}
			
		});
		
		ok_button = new JButton("Ok");
		ok_button.setBounds(new Rectangle(234, 165, 70, 31));
		ok_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// richiamo l'apply prima di cambiare il panel
				apply_button.doClick();
				dump();
				panelChange(app, mainContainer);
			}
		});

		/*
		 * Wallpaper
		 */
		JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/back3.jpg")));
		backgroundLabel.setBounds(0, 0, WIDTH, HEIGHT);

		/*
		 * Populate ;)
		 */
		//device
		optionContainer.add(deviceLabel);
		optionContainer.add(deviceRefreshButton);
		optionContainer.add(deviceCombo);
		optionContainer.add(deviceStatus);
		//midi
		optionContainer.add(midiLabel);
		optionContainer.add(midiRefreshButton);
		optionContainer.add(midiCombo);
		optionContainer.add(midiStatus);
		//buttons
		optionContainer.add(cancel_button);
		optionContainer.add(apply_button);
		optionContainer.add(ok_button);
		//background
		optionContainer.add(backgroundLabel);

	}

	private void addOptionFieldBlock(Point vertice, String label,
			ActionListener dropdownCallback) {
		deviceRefreshButton = new JButton();
		deviceRefreshButton.setBounds(new Rectangle(((int)vertice.getX()-20),((int)vertice.getY()+25),13,13)); // -20,25
/////////////////////////		
		deviceRefreshButton.setEnabled(false);
/////////////////////////
		deviceRefreshButton.setIcon(new ImageIcon(getClass().getResource("/resources/refresh.png")));
		deviceRefreshButton.addActionListener(dropdownCallback);
			
		
		
		deviceStatus = new JLabel(new ImageIcon(getClass().getResource("/resources/status_off_18.png")));
		deviceStatus.setBounds(new Rectangle(((int)vertice.getX()+235),((int)vertice.getY()+20),18,18)); // 235,20
		
		deviceLabel = new JLabel(label);
		deviceLabel.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()), 270, 16)); // 0,0
		deviceCombo = new JComboBox();
		deviceCombo.setBounds(new Rectangle(((int)vertice.getX()), ((int)vertice.getY()+20), 226, 22)); // 0,20
//		deviceComboRefresh();
	}

	private void initMainContainer() {

		initLocations();

		mainContainer = new JPanel();
		mainContainer.setLayout(null);

		button = new JToggleButton[B_NUMBER];
		toggle = new JToggleButton[B_NUMBER]; 
		reset = new JCheckBox[B_NUMBER];
		resetMask = new boolean[B_NUMBER];

//		Image on = on_state.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
//		on_state = new ImageIcon(on);
		ImageIcon on_state = new ImageIcon(getClass().getResource("/resources/Aled_on_36.png"));
		
		ImageIcon off_state = new ImageIcon(getClass().getResource("/resources/Aled_off_36.png"));

		ImageIcon toggle_on_state =  new ImageIcon(getClass().getResource("/resources/tgl_led_on2.png"));
		
		ImageIcon toggle_off_state =  new ImageIcon(getClass().getResource("/resources/tgl_led_off2.png"));

		ImageIcon status_on_state = new ImageIcon(getClass().getResource("/resources/status_on_18.png"));
			
		ImageIcon status_off_state = new ImageIcon(getClass().getResource("/resources/status_off_18.png"));
		
		/*
		 *  Shortcut nascosta per l'aggiornamento della resetmask
		 */
		refreshResetMask = new JMenuItem();
		refreshResetMask.setAccelerator(
				// CTRL + S
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
		refreshResetMask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.err.println("[Ctrl+S] pressed: reset mask updated");
				for(int i = 0 ; i < B_NUMBER ; i++) {
					resetMask[i] = button[i].isSelected();
				}
			}
		});
		//////////////////////////////////////////////////////////////////

		opt_button = new JButton();
		opt_button.setBounds(new Rectangle(359, 197, 16, 16));
		opt_button.setBorder(BorderFactory.createEmptyBorder());
		opt_button.setContentAreaFilled(false);
		opt_button.setIcon(new ImageIcon(getClass().getResource("/resources/opt.png")));
		opt_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("switch");
				
				panelChange(app, optionContainer);
				
			}
		});
		
		for(int i = 0; i < B_NUMBER ; i++) {
			
			//inizializzazione pulsanti
			button[i] = new JToggleButton(off_state);
			button[i].setSelectedIcon(on_state);
			button[i].setRolloverEnabled(false);
			button[i].setBorder(BorderFactory.createEmptyBorder());
			button[i].setContentAreaFilled(false);
			button[i].setBounds(buttonLocation[i]);
			button[i].setFocusable(false);
			
			//inizializzazione pulsanti retro
			toggle[i] = new JToggleButton(toggle_off_state);
			toggle[i].setBounds(toggleLocation[i]);
			toggle[i].setFocusable(false);
			toggle[i].setBorder(BorderFactory.createEmptyBorder());
			toggle[i].setContentAreaFilled(false);
			toggle[i].setSelectedIcon(toggle_on_state);
			toggle[i].setRolloverEnabled(false);
			
			//inizializzazione check box
			reset[i] = new JCheckBox("Reset");
			reset[i].setBounds(resetLocation[i]);
			reset[i].setFocusable(false);
			
			//popolamento panel
			mainContainer.add(button[i]);
			mainContainer.add(toggle[i]);
			mainContainer.add(reset[i]);
			
		}
		
		mainContainer.add(refreshResetMask);
		mainContainer.add(opt_button);
		
		// SFONDO....(trovare qualcosa di meglio..se possibile)
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("/resources/back3.jpg")));
		label.setBounds(0, 0, WIDTH, HEIGHT);
		mainContainer.add(label);
		

		
	}
	
	private void panelChange(JFrame app, JPanel panel){
		app.setContentPane(panel);
		app.validate();
	}

	private void deviceComboRefresh() {
		//TODO: scrivere sto coso..
		net.java.games.input.Controller[] cs = 
			ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		deviceCombo.removeAllItems();
		for(int i=0;i<cs.length;i++)
			deviceCombo.addItem(cs[i].getName()+","+cs[i].getType());
	}
	
	private void midiComboRefresh(){
		
		Info[] midi = MidiSystem.getMidiDeviceInfo();
		
		midiCombo.removeAllItems();
		
		for(int i = 0;i<midi.length;i++){
			try {
				if(MidiSystem.getMidiDevice(midi[i]).getMaxReceivers() != 0){
					midiCombo.addItem(midi[i].getName());
				}
			} catch (MidiUnavailableException e) { System.err.println(e);}
		}
	}
	
	private JFrame getApplication() {
		
		init();
		
		app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(WIDTH, HEIGHT);
		app.setContentPane(mainContainer);
		app.setTitle("Stomp Controller");
		app.setResizable(false);
		
		workerThread = new PedalController2(button,toggle,reset,resetMask);

		load();

		workerThread.start();

		return app;
	}

	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException e) {} 
		catch (InstantiationException e) {} 
		catch (IllegalAccessException e) {} 
		catch (UnsupportedLookAndFeelException e) {}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ControlGui application = new ControlGui();
				application.getApplication().setVisible(true);
			}
		});
	}

}
