package compactController;

import java.util.Vector;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class Recognizer {

	
	private static class ControllerDevice {
		public Controller device;
		public EventQueue eQueue;
	}
	
	
	private static ControllerDevice controller = new ControllerDevice();
	private static final int SLEEP_TIME = 50;
	
	
	public static boolean setDevice(String name) {

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
	
	
	private static void refreshEventQueue(Vector<Event> queue){
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
	
	public static Controller getDeviceByName(String name) {
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
	
	
	
	
	
	
	
	public static void main(String args[]) {
		
		Vector<Event> queue = new Vector<Event>();
		Vector<String> nomi = new Vector<String>();
		
		if(!setDevice("USB Joystick      USB Joystick     ")) {
			System.err.println("Controller not found!");
			return;
		}
		
		//ripulisco
		refreshEventQueue(queue);

		while(true) {
			refreshEventQueue(queue);
    		
    		//inizio a leggere
    		for(Event event : queue){

    			StringBuffer buffer = new StringBuffer();
    			
    			Component comp = event.getComponent();
    			
    			if( !nomi.contains( comp.getIdentifier().toString() ) ){
    				nomi.add(comp.getIdentifier().toString());
    				buffer.append("aggiunto "+comp.getIdentifier().toString());
    				System.out.println(buffer.toString());
    			}
    		}
    		try {
    			Thread.sleep(SLEEP_TIME);
    		}catch(InterruptedException e) {}
		}
		
		
		
	}
	
}
