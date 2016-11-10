package org.systemexception.adtrap.pojo.logtailer;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class LogTailer extends Tailer {

	private static final boolean TAIL_FROM_END_OF_FILE = true;

	@Autowired
	public LogTailer(File file, TailerListener listener, long delayMillis) {

		super(file, listener, delayMillis, TAIL_FROM_END_OF_FILE);
	}
}
