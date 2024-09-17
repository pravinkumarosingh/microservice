package com.service.product;

import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApplicationTests {

	@ServiceConnection
	private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.1.13");

	@LocalServerPort
	Integer port;

	static {
		mongoDBContainer.start();
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """
					{
				     "name" : "oneplus 12",
				     "description" : "smartphone",
				     "price" : "46000"
				 }
				""";
		RestAssured.given()
				.accept("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id" , (ResponseAwareMatcher<Response>) Matchers.notNullValue())
				.body("name" , (ResponseAwareMatcher<Response>) Matchers.equalTo("oneplus 12"))
				.body("description" , (ResponseAwareMatcher<Response>) Matchers.equalTo("smartphone"))
				.body("price" , (ResponseAwareMatcher<Response>) Matchers.equalTo(46000));


	}

}
