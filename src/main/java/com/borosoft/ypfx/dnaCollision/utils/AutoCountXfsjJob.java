package com.borosoft.ypfx.dnaCollision.utils;

import com.borosoft.ypfx.dnaCollision.jservice.DnaCollisionJservice;
import com.borosoft.ypfx.dnaCollision.thread.AutoCountXfsjThread;

public class AutoCountXfsjJob {
	
	private DnaCollisionJservice dnaCollisionJservice;
	
	
	public void execute() {
		AutoCountXfsjThread autoCountXfsjThread = new AutoCountXfsjThread(dnaCollisionJservice);
		Thread thread = new Thread(autoCountXfsjThread);
		thread.start();
	}


	public DnaCollisionJservice getDnaCollisionJservice() {
		return dnaCollisionJservice;
	}


	public void setDnaCollisionJservice(DnaCollisionJservice dnaCollisionJservice) {
		this.dnaCollisionJservice = dnaCollisionJservice;
	}
	
	
	
	
	

}
