package com.borosoft.ypfx.dnaCollision.thread;

import com.borosoft.ypfx.dnaCollision.jservice.DnaCollisionJservice;

public class AutoCountXfsjThread implements Runnable{
	
	private DnaCollisionJservice dnaCollisionJservice;
	

	public AutoCountXfsjThread(DnaCollisionJservice dnaCollisionJservice) {
		this.dnaCollisionJservice = dnaCollisionJservice;
	}

	@Override
	public void run() {
		try {
			dnaCollisionJservice.countTimeLimit();;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DnaCollisionJservice getDnaCollisionJservice() {
		return dnaCollisionJservice;
	}

	public void setDnaCollisionJservice(DnaCollisionJservice dnaCollisionJservice) {
		this.dnaCollisionJservice = dnaCollisionJservice;
	}
	
	

}
