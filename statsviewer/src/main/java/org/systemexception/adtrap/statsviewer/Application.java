package org.systemexception.adtrap.statsviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author leo
 * @date 07/10/2016 17:14
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
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
