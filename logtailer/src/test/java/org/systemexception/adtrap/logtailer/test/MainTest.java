package org.systemexception.adtrap.logtailer.test;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.systemexception.adtrap.logtailer.main.Main;

import java.io.FileNotFoundException;

public class MainTest {

	@Test(expected = MissingOptionException.class)
	public void should_return_usage() throws FileNotFoundException, InterruptedException, ParseException {
		Main.main(new String[0]);
	}

	@Test(expected = NumberFormatException.class)
	public void should_accept_numbers_for_polling() throws FileNotFoundException, InterruptedException, ParseException {
		Main.main(new String[]{"-f somefile.log", "-s abc"});
	}

	@Test(expected = FileNotFoundException.class)
	public void should_refuse_non_existing_file() throws FileNotFoundException, InterruptedException, ParseException {
		Main.main(new String[]{"-f", "somefile.log", "-s", "500"});
	}

}