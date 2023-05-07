package org.systemexception.adtrap.pojo;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author leo
 * @date 10/11/2016 19:11
 */
@Component
public class LogQueue extends LinkedBlockingQueue<String> {

	private LinkedBlockingQueue<String> blockingQueue;

	public LinkedBlockingQueue<String> getBlockingQueue() {
		return blockingQueue;
	}

	public void setBlockingQueue(LinkedBlockingQueue<String> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
}
