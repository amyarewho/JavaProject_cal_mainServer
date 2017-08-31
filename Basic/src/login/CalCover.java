package login;

import java.io.Serializable;

public class CalCover implements Serializable{
	private CallenderVO v;
	private String etc;
	private int command;
	public CalCover(){
		
	}
	
	public CalCover(CallenderVO v, int command , String etc){
		this.v = v;
		this.command = command;
		this.etc = etc;
	}
	public CallenderVO getV() {
		return v;
	}
	public void setV(CallenderVO v) {
		this.v = v;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}

	
	

}
