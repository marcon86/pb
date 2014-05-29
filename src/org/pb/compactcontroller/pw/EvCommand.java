package org.pb.compactcontroller.pw;

public class EvCommand {

	public static final int LOCK = -1;
	public static final int BTN_CHANGE = 0;
	
	private int codOp;
	private int index;
	private boolean value;

	public EvCommand() {}

	public int getCodOp() {
		return codOp;
	}
	
	public EvCommand(int codOp, int index, boolean value) {
		this.codOp = codOp;
		this.index = index;
		this.value = value;
	}

	public void setCodOp(int codOp) {
		this.codOp = codOp;
	}
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public static EvCommand wrapSendBtnChange(int index, boolean value) {
		return new EvCommand(BTN_CHANGE, index, value);
	}
	
	public static EvCommand wrapLockStatus(boolean value) {
		return new EvCommand(LOCK, -1, value);
	}

}
