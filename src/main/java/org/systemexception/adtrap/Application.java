package org.systemexception.adtrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.systemexception.adtrap.pojo.LogQueue;
import org.systemexception.adtrap.pojo.logtailer.LogTailer;
import org.systemexception.adtrap.pojo.logtailer.LogTailerListener;
import org.systemexception.adtrap.service.DataService;
import org.systemexception.adtrap.service.DhcpLeasesReader;
import org.systemexception.adtrap.service.LogTailerBridge;
import org.systemexception.adtrap.service.MySqlDataService;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author leo
 * @date 07/10/2016 17:14
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	public static final String CONTEXT = "logarchiver";

	@Value("${adtrap.ipaddress}")
	private String ipAddress;

	@Value("${dnsmasq.dhcp.leases.file.path}")
	private String dnmasqDhcpLeasesFilePath;

	@Value("${dnsmasq.log.file.path}")
	private String dnsmasqLogFilePath;

	@Value("${dnsmasq.tailer.sleep}")
	private int dnsmasqTailerSleep;

	@Value("${home.domain}")
	private String homeDomain;

	@Value("${ignore.list}")
	private String[] ignoreList;

	private final LogQueue logQueue;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public Application(LogQueue logQueue, JdbcTemplate jdbcTemplate) {
		this.logQueue = logQueue;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Main entry point
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataService dataService() {
		return new MySqlDataService(jdbcTemplate, ipAddress, homeDomain);
	}

	@Bean
	public DhcpLeasesReader dhcpLeasesReader() throws URISyntaxException {
		return new DhcpLeasesReader(dnmasqDhcpLeasesFilePath);
	}

	@Bean
	public LogTailerBridge logTailerBridge() {
		return new LogTailerBridge(dataService(), logQueue, Arrays.asList(ignoreList));
	}

	@Bean
	public Docket restfulApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("restful-api").select().build().apiInfo(apiInfo());
	}

	@PostConstruct
	public void postConstruct() throws FileNotFoundException {
		File fileToTail = new File(dnsmasqLogFilePath);
		if (!fileToTail.exists()) {
			throw new FileNotFoundException(fileToTail.getAbsolutePath());
		} else {
			LOGGER.info("Tailing file " + dnsmasqLogFilePath);
		}

		LogTailerListener logTailerListener = new LogTailerListener(logQueue);
		LogTailer logTailer = new LogTailer(fileToTail, logTailerListener, dnsmasqTailerSleep);

		new Thread(logTailer).start();
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Adtrap",
				"Adtrap Application",
				null,
				null,
				"https://github.com/lcappuccio/filtering-ads-with-linux/",
				"GPL v3",
				"https://github.com/lcappuccio/filtering-ads-with-linux/blob/master/LICENSE"
		);
	}
}
