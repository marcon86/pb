package alg;

import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import net.java.games.input.Component;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class Cont extends Thread {

	private final int SLEEP_TIME = 50;
	
	private JToggleButton[] button;
	private JCheckBox[] toggle;
	private boolean[] value;
	String[] bName;
	String[] bNameL = { "Base", "z", "Pinkie", "Top 2", "rx", "Base 2" };
	String[] bNameOW = { "6", "x", "5", "4", "y", "7" };	
	
	public Cont(JToggleButton[] button) {
		this.button = button;
		value = new boolean[] {false,false,false,false,false,false};
		String osName = System.getProperty("os.name"); 

        if(osName.contains("Windows")) 
        	bName = bNameOW;
        else if(osName.contains("Linux")) 
        	bName = bNameL;
        else if(osName.contains("Mac OS")) 
        	bName = bNameOW;
        else {
        	bName = bNameL;
        	System.out.println("Unable to recognize Operating System");
        }
	}
	
	public Cont(JToggleButton[] button, JCheckBox[] toggle) {
		this(button);
		this.toggle = toggle;
	}
	
	public int getValue(String name) {

		int i = 0;

		while (i < bName.length) {
			if (name.equals(bName[i]))
				return i + 1;
			else
				i++;
		}
		return -1;
	}

	public void run() {
		
		net.java.games.input.Controller[] cs = ControllerEnvironment.getDefaultEnvironment().getControllers();
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
			System.err.println("PeDaLiera NON TROVATA");
			for(int i=0;i<button.length;i++) {
				button[i].setEnabled(false);
				toggle[i].setEnabled(false);
			}
			return;
		}
		
		// -------------- //
		System.out.println(pad.getName() + ", " + pad.getType());
		System.out.println("--------------------------");


		EventQueue queue = pad.getEventQueue();
		Event event = new Event();

		/*
		 * INIZIO MONITORAGGIO
		 */
		while (true) {

			pad.poll();
			while (queue.getNextEvent(event)) {
				StringBuffer buffer = new StringBuffer();
				Component comp = event.getComponent();
				buffer.append(comp.getName()).append(" -> ");
				float value = event.getValue();
				try {
					if ( (comp.isAnalog() && value == -1.0f) ||
						 (!comp.isAnalog() && value == 1.0f) ) { 
						// buffer.append(value);
						buffer.append("On --:>" + getValue(comp.getName()));
						if(toggle[getValue(comp.getName())-1].isSelected()) {
							int i = getValue(comp.getName())-1;
							this.value[i] = !this.value[i];
							button[getValue(comp.getName())-1].setSelected(this.value[i]);
						}else
							button[getValue(comp.getName())-1].setSelected(true);
							
					} else {
						buffer.append("Off");
						if(!toggle[getValue(comp.getName())-1].isSelected())
							button[getValue(comp.getName())-1].setSelected(false);
					}
					System.out.println(buffer.toString());
				}catch(ArrayIndexOutOfBoundsException e) {/* ai primi poll le cose vanno male */}
			}

			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {}
		}
	}
}
