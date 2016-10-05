package org.systemexception.adtrap.logtailer.services;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.io.File;

public class LogTailer extends Tailer {

	public LogTailer(File file, TailerListener listener, long delayMillis) {
		super(file, listener, delayMillis);
	}
}
