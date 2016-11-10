package org.systemexception.adtrap.pojo.logtailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.systemexception.adtrap.model.DnsLogLine;
import org.systemexception.adtrap.pojo.JsonMapper;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.service.DataService;

import java.text.ParseException;
import java.util.Optional;

/**
 * @author leo
 */
public class LogTailerBridge implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogTailerBridge.class);
	private final DataService dataService;
	private final LogQueue logQueue;
	private final JsonMapper jsonMapper = new JsonMapper();

	@Autowired
	public LogTailerBridge(DataService dataService, final LogQueue logQueue) {
		this.logQueue = logQueue;
		this.dataService = dataService;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String logLine = (String) logQueue.take();
				Optional<DnsLogLine> dnsLogLine = jsonMapper.dnsLogLineFromLogLine(logLine);
				if (dnsLogLine.isPresent()) {
					dataService.save(dnsLogLine.get());
				} else {
					LOGGER.info("Bad line caught, skipped: " + logLine);
				}
			}
		} catch (InterruptedException | ClassCastException | ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
