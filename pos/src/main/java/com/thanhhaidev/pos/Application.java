package com.thanhhaidev.pos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.thanhhaidev.pos.model.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({ FileStorageProperties.class })
@Configuration
@EnableScheduling
public class Application {

	private static final String PATH_FILE = "E:/POS_System/pos/src/main/resources/database/";
	private static final String URL_PRODUCT = "http://localhost:4000/api/v1/products";

	@Scheduled(fixedDelay = 1000 * 60 * 3, initialDelay = 1000)
	public void scheduleTaskSaveData() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();

		JSONParser parser = new JSONParser();
		try {
			JSONArray a = (JSONArray) parser
					.parse(new FileReader(PATH_FILE + "product_" + dtf.format(now) + "_1.json"));
			for (Object o : a) {
				JSONObject product = (JSONObject) o;
				String name = (String) product.get("name");
				int price = (int) product.get("price");

				String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU1Njg1NTIwM30.wk7OkCPC8M_-2bZpt0Q_B2Wv9eEaSEQDSlxEILbnyuhs0Yko2Jl-xODsgpaaC3X_CnDfmkd8NzcPRVtcfAq6pQ";

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add("User-Agent", "Spring's RestTemplate"); // value can be whatever
				headers.add("Authorization", "Bearer " + authToken);

				JSONObject request = new JSONObject();
				request.put("name", name);
				request.put("price", price);

				HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

				// send request and parse result
				ResponseEntity<String> response = restTemplate.exchange(URL_PRODUCT, HttpMethod.POST, entity,
						String.class);
				if (response.getStatusCode() == HttpStatus.OK) {
					System.out.println("success");
				} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					// nono... bad credentials
				}
			}

		} catch (FileNotFoundException | ParseException e) {
			e.printStackTrace();
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
