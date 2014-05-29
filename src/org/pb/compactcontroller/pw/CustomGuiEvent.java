package org.pb.compactcontroller.pw;

import java.util.Observable;

public class CustomGuiEvent extends Observable {
	
	public CustomGuiEvent() {}
	
	public synchronized void sendChange(int index, boolean value) {
		this.fireNotify( EvCommand.wrapSendBtnChange(index, value) );
	}

	public synchronized void sendLock() {
		this.fireNotify( EvCommand.wrapLockStatus(true) );		
	}
	
	public synchronized void sendUnlock() {
		this.fireNotify( EvCommand.wrapLockStatus(false) );				
	}
	
	private void fireNotify(EvCommand command) {
		this.setChanged();
		this.notifyObservers( command );
	}
}

