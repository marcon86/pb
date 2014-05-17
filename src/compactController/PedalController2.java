package compactController;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;

public class PedalController2 extends Thread {  
	
	private class ControllerDevice {
		public Controller device;
		public EventQueue eQueue;
	}
	
	private class MidiOut {
		public MidiDevice midiOut;
		public boolean isActive;
		public MidiOut() { midiOut = null; isActive = false; }
	}

	private final int CHANNEL = 1;
	
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
	
	private ControllerDevice controller;
	private MidiOut midi;
	
	private PedalController2() {
		
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
        
        midi = new MidiOut();
        controller = new ControllerDevice();
	}
	
	public PedalController2(JToggleButton[] button, JToggleButton[] toggle, JCheckBox[] reset, boolean[] resetMask) {
		this();
		this.button = button;
		this.toggle = toggle;
		this.reset = reset;
		this.resetMask = resetMask;
	}
	
	
	public PedalController2(JToggleButton[] button, JToggleButton[] toggle, JCheckBox[] reset, boolean[] resetMask, String midiDeviceName){
		this(button,toggle,reset,resetMask);
		midi.midiOut = openMidiByName(midiDeviceName);
		if(midi.midiOut != null)
			midi.isActive = true;
	}
	
	public Controller getDevice() {
		Controller ret;
		
		synchronized(controller){
			ret = controller.device;
		}
		
		return ret;
	}
	
	public boolean setDevice(String name) {

		Controller tmp = getDeviceByName(name);
		
		if(tmp != null) {

			synchronized(controller) {
				controller.device = tmp;
				controller.eQueue = controller.device.getEventQueue();
			}
			
			return true;
		}else
			return false;
		
	}
	
	///
	// NOOOOOO.... riempie la ram di mondezza.....
	//
	//		:::FA VERAMENTE SCHIFO:::
	///
	private void refreshEventQueue(Vector<Event> queue){
		Event e = new Event();
		queue.clear();
		synchronized(controller) {
			controller.device.poll();
			while(controller.eQueue.getNextEvent(e)) {
				queue.add(e);
				e = new Event();
			}
		}
	}
	
	public Controller getDeviceByName(String name) {
		net.java.games.input.Controller[] cs = 
			ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		Controller tmp = null;
		
		for(int i = 0 ; i<cs.length;i++)
			System.out.println(cs[i].getName());
		
		// cerco la pedaliera
		{
    		int i=0;
    		boolean found = false;
    		while (i<cs.length && !found) {
    			if(cs[i].getName().startsWith(name)) {
    				found = true;
    				tmp = cs[i];
    			}else
    				i++;
    		}
		}
		
		return tmp;
	}
	
	public void setButtonMask(String[] bMask) {
		synchronized(bName) {
			bName = bMask;
		}
	}
	
	public String[] getButtonMask() {
		synchronized(bName) {
			return bName;
		}
	}
	
	public MidiDevice getMidiOut() {
		MidiDevice tmp;
		synchronized(midi) {
			tmp = midi.midiOut;
		}
		return tmp;
	}
	
	public boolean setMidiOut(String deviceName){
		
		MidiDevice tmp = openMidiByName(deviceName);
		
		if(tmp != null){
			synchronized(midi) {
    			midi.midiOut = tmp;
    			midi.isActive = true;
			}
			return true;
		}else
			return false;	
    	
	}
	
	public boolean isMidiActive(){
		boolean tmp;
		synchronized(midi) {
			tmp = midi.isActive;
		}
		return tmp;
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

		synchronized(midi) {
			if(!midi.isActive)
				return false;
		}	
		
		boolean res = false;		
		
		ShortMessage m = new ShortMessage();
		try {
			m.setMessage(ShortMessage.CONTROL_CHANGE, CHANNEL, ( CC + CC_Offset ), translate(val));
		} catch (InvalidMidiDataException e) {return false;}
		
		synchronized(midi) {
    		try {
    			midi.midiOut.getReceiver().send(m, -1);
    			res = true;
    		} catch (MidiUnavailableException e) {
    			res = false;
    		}
		}
		
		return res;
	}
	
	public int getIndex(Component button) {
		//sarebbe meglio farlo con delle hashmap
		String name = button.getIdentifier().toString();
		
		int i = 0;
		
		synchronized(bName) {
    		while (i < bName.length) {
    			if (name.equals(bName[i]))
    				return i + 1;
    			else
    				i++;
    		}
		}
		return -1;
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
		
		Vector<Event> queue = new Vector<Event>();
		
		if(!setDevice("USB Joystick")) {			// BAAAAAAD IDEA !!! 
			lock(true,"Controller not found!");
			return;
		}
		
		
/* Ripulisco gli eventi dagli errori analogici iniziali */
		refreshEventQueue(queue);
/*             ****************************             */
		/*
		 * INIZIO MONITORAGGIO
		 */
		while (true) {
			// ad ogni giro faccio il poll e prelevo i nuovi eventi
			refreshEventQueue(queue);

			//prendo l'identifier e non il nome perchè più portabile
			// su windows il nome cambia anche con la lingua del sistema
			for(Event event : queue){
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
