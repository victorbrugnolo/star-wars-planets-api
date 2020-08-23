package dev.victorbrugnolo.starwarsplanets.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.victorbrugnolo.starwarsplanets.StarWarsPlanetsApplication;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.exceptions.ErrorMessage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = StarWarsPlanetsApplication.class)
public class PlanetResourceTest {

  @Value("${local.server.port}")
  private int generatedPort;

  private static final String PLANETS_PATH = "/planets/";
  private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

  private static final String PLANET_CLIMATE = "temperate";
  private static final String PLANET_TERRAIN = "grasslands, mountains";

  @BeforeAll
  public void setUp() {
    RestAssured.port = generatedPort;
    RestAssured.baseURI = "http://localhost/api/";
  }

  @Test
  public void shouldObtainConflictExceptionWhenSavePlanet() {
    String planetName = "Kamino";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .body(planetRequest)
        .when()
        .post(PLANETS_PATH)
        .thenReturn();

    Response response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(planetRequest)
            .when()
            .post(PLANETS_PATH)
            .thenReturn();

    assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
  }

  @Test
  public void shouldObtainBadRequestExceptionWhenSavePlanet() {
    PlanetRequest planetRequest = PlanetRequest.builder()
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .body(planetRequest)
        .when()
        .post(PLANETS_PATH)
        .thenReturn();

    Response response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(planetRequest)
            .when()
            .post(PLANETS_PATH)
            .thenReturn();

    ErrorMessage errorMessage = response.getBody().as(ErrorMessage.class);

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    assertEquals("Invalid request", errorMessage.getMessage());
    assertTrue(errorMessage.getErrors().contains("name: must not be null"));
    assertTrue(errorMessage.getErrors().contains("name: must not be empty"));
  }

  @Test
  public void shouldObtainBadRequestExceptionWhenSavePlanetInexistentInSwApi() {
    String planetName = "wrong name";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(planetRequest)
            .when()
            .post(PLANETS_PATH)
            .then();

    response.assertThat().statusCode(HttpStatus.NOT_FOUND.value())
        .body(containsString("Planet " + planetName + " not found in SWAPI"));
  }

  @Test
  public void shouldCreatePlanet() {
    String planetName = "Alderaan";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    Response response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(planetRequest)
            .when()
            .post(PLANETS_PATH)
            .thenReturn();

    String locationPattern =
        "http://localhost:" + RestAssured.port + "/api" + PLANETS_PATH + UUID_REGEX;

    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    assertTrue(response.header(HttpHeaders.LOCATION).matches(locationPattern));
  }

  @Test
  public void shouldGetAllPlanetsFromDatabase() {
    String planetName = "Kashyyyk";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    Response saved =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(planetRequest)
            .when()
            .post(PLANETS_PATH)
            .thenReturn();

    String header = saved.getHeader(HttpHeaders.LOCATION);
    String itemSavedId = getIdFromHeader(header);

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH)
            .then();

    response.assertThat().statusCode(HttpStatus.OK.value()).body("content.size()", greaterThan(0))
        .body(containsString(itemSavedId)).body(containsString(planetName));
  }

  @Test
  public void shouldGetPlanetsFromSwApi() {
    String planetName = "Hoth";

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH + "swapi")
            .then();

    response.assertThat().statusCode(HttpStatus.OK.value()).body("content.size()", greaterThan(0))
        .body(containsString(planetName));
  }

  @Test
  public void shouldObtainNotFoundStatusWhenGetPlanetByName() {
    String planetName = "unexists";

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH + "?name=" + planetName)
            .then();

    response.assertThat().statusCode(HttpStatus.NOT_FOUND.value())
        .body(containsString("Planet " + planetName + " not found"));
  }


  @Test
  public void shouldGetPlanetByName() {
    String planetName = "Dagobah";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .body(planetRequest)
        .when()
        .post(PLANETS_PATH)
        .thenReturn();

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH + "?name=" + planetName)
            .then();

    response.assertThat().statusCode(HttpStatus.OK.value())
        .body(containsString(planetName)).body(containsString("\"id\":"));
  }

  @Test
  public void shouldObtainNotFoundStatusWhenGetPlanetById() {
    String planetId = "inexistentId";

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH + planetId)
            .then();

    response.assertThat().statusCode(HttpStatus.NOT_FOUND.value())
        .body(containsString("Planet id " + planetId + " not found"));
  }

  @Test
  public void shouldGetPlanetById() {
    String planetName = "Endor";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    Response saved = given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .body(planetRequest)
        .when()
        .post(PLANETS_PATH)
        .thenReturn();

    String header = saved.getHeader(HttpHeaders.LOCATION);
    String itemSavedId = getIdFromHeader(header);

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(PLANETS_PATH + itemSavedId)
            .then();

    response.assertThat().statusCode(HttpStatus.OK.value())
        .body(containsString(planetName)).body(containsString(itemSavedId));
  }

  @Test
  public void shouldObtainNotFoundStatusWhenDeletePlanet() {
    String planetId = "inexistentId";

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(PLANETS_PATH + planetId)
            .then();

    response.assertThat().statusCode(HttpStatus.NOT_FOUND.value())
        .body(containsString("Planet id " + planetId + " not found"));
  }

  @Test
  public void shouldDeletePlanet() {
    String planetName = "Coruscant";

    PlanetRequest planetRequest = PlanetRequest.builder().name(planetName)
        .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

    Response saved = given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .body(planetRequest)
        .when()
        .post(PLANETS_PATH)
        .thenReturn();

    String header = saved.getHeader(HttpHeaders.LOCATION);
    String itemSavedId = getIdFromHeader(header);

    ValidatableResponse response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(PLANETS_PATH + itemSavedId)
            .then();

    response.assertThat().statusCode(HttpStatus.NO_CONTENT.value());
  }

  private String getIdFromHeader(String header) {
    return header.substring(header.indexOf("/planets/")).replace(PLANETS_PATH, "");
  }

}
