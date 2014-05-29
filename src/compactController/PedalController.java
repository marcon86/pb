package compactController;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class PedalController extends Thread {  

	public final static int MIDI_SET = 0;
	public final static int MIDI_GET = 1; 
	public final static int MIDI_SEND = 2;
	public final static int MIDI_NAME = 3;
	public final static int MIDI_STATUS = 4;
	
	public final static int MIDI_TRUE = 127;
	public final static int MIDI_FALSE = 0;

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
		if(val) return MIDI_TRUE;
		else	return MIDI_FALSE;
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
	
	public int getComponentIndex(Component button) {
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
		
		Controller pad = this.getDeviceController();
		if(pad == null) {
			lock(true,"PeDaLiera NON TROVATA");
			return;
		}

//		if(getMidiOut() == null) setMidiOut("Bus 1");
		
		// -------------- //
		System.out.println(pad.getName() + ", " + pad.getType());
		System.out.println("--------------------------");

		/* Ripulisco gli eventi dagli errori analogici iniziali */
		this.clearControllerEventQueue(pad);

		EventQueue evQueue = pad.getEventQueue();
		Event event = new Event();
		while (true) {

			if(pad.poll() == false) {
				this.lock(true,null);
				break;
			}

			while (evQueue.getNextEvent(event))
				this.handleDeviceEvent(event);

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {}
		}
	}

	private void clearControllerEventQueue(Controller pad) {
		Event evt = new Event();

		EventQueue q = pad.getEventQueue();
		pad.poll();
		while(q.getNextEvent(evt));
	}

	private void handleDeviceEvent(Event event) {
		StringBuffer buffer = new StringBuffer();
		Component comp = event.getComponent();
		buffer.append(comp.getIdentifier().toString()).append(" -> ");
		float value = event.getValue();
		int i = getComponentIndex(comp)-1;

		if(i!=-1) {
			// ON
			if ( (comp.isAnalog() && value == -1.0f) || (!comp.isAnalog() && value == 1.0f) ) { 
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

	private Controller getDeviceController() {
		Controller[] cs = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		for(Controller c : cs) {
			System.out.println(c.getName());
			if(c.getType().toString().equalsIgnoreCase("Stick")) {
				return c;
			}
		}
		
		return null;
	}
}
