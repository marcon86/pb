package alg;

import net.java.games.input.*;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class TestReconnect {
	public static void main(String[] args) throws InterruptedException {
		
		net.java.games.input.Controller[] cs = 
			new LinuxEnvironmentPlugin().getControllers();
			//net.java.games.input.LinuxEnvironmentPlugin.getDefaultEnvironment().getControllers();
			//ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		for(int i = 0 ; i<cs.length;i++)
			System.out.println(cs[i].getName());
		
		
		System.out.println("PAUSA");
		Thread.sleep(7000);
		
		
		net.java.games.input.Controller[] cs1 = 
			new LinuxEnvironmentPlugin().getControllers();
		
		for(int i = 0 ; i<cs1.length;i++)
			System.out.println(cs1[i].getName());
	}
}
