package org.systemexception.adtrap.logtailer.services;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.util.ArrayList;
import java.util.List;

public class LogTailerListener implements TailerListener {

	private List<String> lines = new ArrayList<>();

	@Override
	public void init(Tailer tailer) {
		lines = new ArrayList<>();
	}

	@Override
	public void fileNotFound() {
		System.out.println("File not found");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void fileRotated() {
		System.out.println("File rotated");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void handle(String line) {
		lines.add(line);
	}

	@Override
	public void handle(Exception ex) {
		System.out.println(ex.getMessage());
	}

	/**
	 * Get the lines in buffer
	 *
	 * @return the lines in buffer
	 */
	public List<String> getLines() {
		return lines;
	}

	/**
	 * Clear the lines in the buffer, to be called after fetching them
	 */
	public void clearLines() {
		lines = new ArrayList<>();
	}
}
