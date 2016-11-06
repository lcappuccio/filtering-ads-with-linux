package org.systemexception.adtrap.logarchiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.systemexception.adtrap.logarchiver.service.DataService;
import org.systemexception.adtrap.logarchiver.service.MySqlDataService;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author leo
 * @date 07/10/2016 17:14
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	@Value("${adtrap.ipaddress}")
	private String ipAddress;
	public static final String CONTEXT = "logarchiver";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataService dataService() {
		return new MySqlDataService(jdbcTemplate, ipAddress);
	}

	@Bean
	public Docket restfulApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("restful-api").select().build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Log Analyzer",
				"Log Analyzer with embedded H2",
				null,
				null,
				"https://github.com/lcappuccio/filtering-ads-with-linux/",
				"GPL v3",
				"https://github.com/lcappuccio/filtering-ads-with-linux/blob/master/LICENSE"
		);
	}
}
