package org.systemexception.adtrap.logtailer.services;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.util.ArrayList;
import java.util.List;

public class LogTailerListener implements TailerListener {

	private List<String> lines = new ArrayList<>();

	@Override
	public void init(Tailer tailer) {
		// TODO implement, if needed
	}

	@Override
	public void fileNotFound() {
		//	TODO implement
	}

	@Override
	public void fileRotated() {
		// TODO implement
	}

	@Override
	public void handle(String line) {
		lines.add(line);
	}

	@Override
	public void handle(Exception ex) {
		// TODO implement
	}

	/**
	 * Get the lines in buffer
	 *
	 * @return
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
