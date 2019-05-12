package com.thanhhaidev.headquarter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.thanhhaidev.headquarter.model.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({ FileStorageProperties.class })
@Configuration
@EnableScheduling
public class Application {

	private static final String URL_PRODUCT = "http://localhost:4100/api/v1/products/brand/";
	private static final String URL_UPLOAD_FILE = "http://localhost:4000/uploadFile";
	private static final String PATH_FILE = "E:/POS_System/headquarter/src/main/resources/database/";

	@Scheduled(fixedDelay = 1000 * 60)
	public void scheduleTaskAsynchronizeData() throws FileExtensionException {
		String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU1ODQwNzMyMn0.Hm5U6NllTJ5socTf6qymvK9hKlxjZnMZn0Oj_t2wD-BpkHfVg42DnI2H-Ytem3aXFKGNIOz-thpjeibhdFDv0Q";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "Spring's RestTemplate"); // value can be whatever
		headers.add("Authorization", "Bearer " + authToken);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(URL_PRODUCT + "1", HttpMethod.GET, new HttpEntity<>("parameters", headers), String.class);

		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();

		System.out.println(PATH_FILE + "product_" + dtf.format(now) + "_1.json");

		body.add("file", getFile(PATH_FILE + "product_" + dtf.format(now) + "_1.json"));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		restTemplate.postForEntity(URL_UPLOAD_FILE, requestEntity, String.class);
	}

	public FileSystemResource getFile(String fileName) throws FileExtensionException {
		return new FileSystemResource(new File(fileName));
	}

	public class FileExtensionException extends Exception {
		private static final long serialVersionUID = 1L;

		public FileExtensionException(String message) {
			super("The selected file has a different extension:" + message);
		}
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
