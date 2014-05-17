package compactController;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import net.java.games.input.Component;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;

public class PedalController extends Thread {  

	public final static int MIDI_SET = 0;
	public final static int MIDI_GET = 1; 
	public final static int MIDI_SEND = 2;
	public final static int MIDI_NAME = 3;
	public final static int MIDI_STATUS = 4;

	private final int CHANNEL = 2;
	
	private final int SLEEP_TIME = 50;
	
	// id linux
	private final String[] bNameL = { "Base", "z", "Pinkie", "Top 2", "rx", "Base 2" };

	// id windows & osx
	private final String[] bNameOW = { "6", "x", "5", "4", "y", "7" };
	
	// il control change avr� CC = CC_Offset + "numero pulsante"
	private final int CC_Offset = 33; 
	
	private JToggleButton[] button;
	private JToggleButton[] toggle; 
	private JCheckBox[] reset;
	private boolean[] resetMask;

	private String[] bName;
	private boolean[] value;
	
	/*
	 * MIDI
	 */
	private boolean midiActive;
	private MidiDevice midiOut;
	
	
	
	private PedalController() {
		
		value = new boolean[] {false,false,false,false,false,false};
		String osName = System.getProperty("os.name"); 

        if(osName.contains("Windows")) 
        	bName = bNameOW;
        else if(osName.contains("Linux")) 
        	bName = bNameL;
        else if(osName.contains("Mac OS")) 
        	bName = bNameOW;
        else {
        	bName = bNameOW;
        	System.out.println("Unable to recognize Operating System");
        }
        midiActive = false;
	}
	
	public PedalController(JToggleButton[] button, JToggleButton[] toggle, JCheckBox[] reset, boolean[] resetMask) {
		this();
		this.button = button;
		this.toggle = toggle;
		this.reset = reset;
		this.resetMask = resetMask;
	}
	
	
	public PedalController(JToggleButton[] button, JToggleButton[] toggle, JCheckBox[] reset, boolean[] resetMask, String midiDeviceName){
		this(button,toggle,reset,resetMask);
		midiOut = openMidiByName(midiDeviceName);
		if(midiOut != null)
			midiActive = true;
	}
	
	
	
	public void setButtonMask(String[] bMask) {
		bName = bMask;
	}
	
	public String[] getButtonMask() {
		return bName;
	}
	
	public MidiDevice getMidiOut() {
		return (MidiDevice) manageMidi(MIDI_GET, null,null);
	}
	
	public boolean setMidiOut(String deviceName){
    	return (Boolean) manageMidi(MIDI_SET, deviceName,null);
	}
	
	public boolean isMidiActive(){
		return (Boolean) manageMidi(MIDI_STATUS,null,null);
	}
	
	private int translate(boolean val) {
		if(val) return 127;
		else	return 0;
	}
	
	public void reset() {
		for(int i = 0; i < value.length; i++) {
			value[i] = resetMask[i];
			if(toggle[i].isSelected())
				button[i].setSelected(resetMask[i]);
		}
	}
	
	private boolean sendMidi(int CC, boolean val){
		if(!isMidiActive())
			return false;
		
		ShortMessage m = new ShortMessage();
		try {
			m.setMessage(ShortMessage.CONTROL_CHANGE, CHANNEL, ( CC + CC_Offset ), translate(val));
		} catch (InvalidMidiDataException e) {return false;}
		
		return (Boolean) manageMidi(MIDI_SEND,null,m);
	}
	
	public int getIndex(Component button) {
		//sarebbe meglio farlo con delle hashmap
		String name = button.getIdentifier().toString();
		
		int i = 0;

		while (i < bName.length) {
			if (name.equals(bName[i]))
				return i + 1;
			else
				i++;
		}
		return -1;
	}

	
	
	private synchronized Object manageMidi(int MODE, String _new_name, ShortMessage _message) {

		Object res = null;
		
		switch(MODE) {
		
			case MIDI_GET:
				res = midiOut;
				break;

			case MIDI_SET:
				MidiDevice tmp = openMidiByName(_new_name);
				if(tmp != null){
					midiOut = tmp;
					midiActive = true;
					res = true;
				}else
					res = false;
				break;		
			case MIDI_SEND:
    			try {
    				midiOut.getReceiver().send(_message, -1);
    				res = true;
    			} catch (MidiUnavailableException e) {
    				res = false;
    			}
    			break;
			case MIDI_STATUS:
				return midiActive;
		}
		
		return res;
	}
	
	private MidiDevice openMidiByName(String name){
		
		Info[] midi = MidiSystem.getMidiDeviceInfo();
		Info out_info = null;
		MidiDevice out = null;
		
		boolean found = false;
		int i = 0;
		
		while(!found && i < midi.length) {
			try {
				//controllo se inizia con name (NON case-sensitive)
				if( midi[i].getName().toUpperCase().startsWith(name.toUpperCase()) ) {
					// ha dei receiver
					if( MidiSystem.getMidiDevice(midi[i]).getMaxReceivers() == -1 ){
						found = true;
						out_info = midi[i];
					}
				}
			} catch (MidiUnavailableException e) {
				out = null;
				System.err.println("error: midi device not found");
			}
			
			i++;
		}
		
		if(out_info != null){
			try {
				out = MidiSystem.getMidiDevice(out_info);
			} catch (MidiUnavailableException e) {
				out = null;
			}
		
    		// OPENING
    		try {
    			out.open();
    		} catch (MidiUnavailableException e) {
    			out = null;
    		}
		}
		return out; 
	}
	
	
	
	public void lock(boolean lock, String reason) {

		if(reason!=null && reason.length()!=0)
			System.err.println(reason);
		
		for(int i=0;i<button.length;i++) {
			button[i].setEnabled(!lock);
			toggle[i].setEnabled(!lock);
			reset[i].setEnabled(!lock);
		}
	}
	
	public void run() {
		
		net.java.games.input.Controller[] cs = 
			ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		net.java.games.input.Controller pad = null;
		
		for(int i = 0 ; i<cs.length;i++)
			System.out.println(cs[i].getName());
		// cerco la pedaliera
		{
    		int i=0;
    		boolean found = false;
    		while (i<cs.length && !found) {
    			if(cs[i].getType().toString().equalsIgnoreCase("Stick")) {
    				found = true;
    				pad = cs[i];
    			}else
    				i++;
    		}
		}
		if(pad == null) {
			lock(true,"PeDaLiera NON TROVATA");
			return;
		}

//////////////////////////////
//////////////////////////////
//		if(getMidiOut() == null) setMidiOut("Bus 1");
		
//////////////////////////////
//////////////////////////////
		
		// -------------- //
		System.out.println(pad.getName() + ", " + pad.getType());
		System.out.println("--------------------------");


		EventQueue queue = pad.getEventQueue();
		Event event = new Event();
		
/* Ripulisco gli eventi dagli errori analogici iniziali */
		pad.poll();
		while(queue.getNextEvent(event));
/*             ****************************             */
		/*
		 * INIZIO MONITORAGGIO
		 */
		while (true) {
			//prendo l'identifier e non il nome perchè più portabile
			// su windows il nome cambia anche con la lingua del sistema
			if(pad.poll() == false) {
				lock(true,null);
				break;
			}
	//		pad.poll();
			while (queue.getNextEvent(event)) {
				
				StringBuffer buffer = new StringBuffer();
				Component comp = event.getComponent();
				buffer.append(comp.getIdentifier().toString()).append(" -> ");
				float value = event.getValue();
				int i = getIndex(comp)-1;

				if(i!=-1) {
					// ON
					if ( (comp.isAnalog() && value == -1.0f) ||
						 (!comp.isAnalog() && value == 1.0f) ) { 
						// buffer.append(value);
						buffer.append((i+1)+" --:> ON");
						if(toggle[i].isSelected()) {
							this.value[i] = !this.value[i];
							button[i].setSelected(this.value[i]);
							sendMidi(i,this.value[i]);
						}else {
							button[i].setSelected(true);
							sendMidi(i,true);
							if(reset[i].isSelected())
								reset();
						}
					// OFF
					} else {
						buffer.append("Off");
						if(toggle[i].isSelected() == false){
							button[i].setSelected(false);
							sendMidi(i,false);
						}
					}
					System.out.println(buffer.toString());
				}else
					System.err.println("not recognized!");
			}

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {}
		}
	}
}
