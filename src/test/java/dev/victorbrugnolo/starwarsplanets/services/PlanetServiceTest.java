package dev.victorbrugnolo.starwarsplanets.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIPlanetResponse;
import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.exceptions.ConflictException;
import dev.victorbrugnolo.starwarsplanets.exceptions.NotFoundException;
import dev.victorbrugnolo.starwarsplanets.repositories.PlanetRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

  @InjectMocks
  private PlanetService planetService;

  @Mock
  private PlanetRepository planetRepository;

  @Mock
  private StarWarsAPIService starWarsAPIService;

  private static final String PLANET_ID = "9ac2b963-6f45-4d12-a6b0-8c1747ac30cb";
  private static final String PLANET_NAME = "Alderaan";
  private static final String PLANET_CLIMATE = "temperate";
  private static final String PLANET_TERRAIN = "grasslands, mountains";
  private static final String PLANET_FILM = "filmUrlInSwApi";
  private static final Integer TOTAL_PLANETS_IN_SWAPI = 60;

  private final Planet planet = Planet.builder()
      .id(UUID.fromString(PLANET_ID)).name(PLANET_NAME)
      .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

  private final PlanetRequest planetRequest = PlanetRequest.builder().name(PLANET_NAME)
      .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).build();

  private final StarWarsAPIPlanetResponse starWarsAPIPlanetResponse = StarWarsAPIPlanetResponse
      .builder().name(PLANET_NAME)
      .climate(PLANET_CLIMATE).terrain(PLANET_TERRAIN).films(Collections.singletonList(PLANET_FILM))
      .build();

  private final StarWarsAPIResponse starWarsAPIResponse = StarWarsAPIResponse.builder().results(
      Collections.singletonList(starWarsAPIPlanetResponse)).count(TOTAL_PLANETS_IN_SWAPI).build();

  @Test
  public void shouldCauseConflictExceptionWhenSavePlanet() {
    when(planetRepository.findByName(anyString())).thenReturn(Optional.of(planet));
    assertThrows(ConflictException.class, () -> planetService.save(planetRequest));
  }

  @Test
  public void shouldCauseNotFoundExceptionOnSwApiWhenSavePlanet() {
    when(planetRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(starWarsAPIService.getPlanetByName(anyString()))
        .thenReturn(ResponseEntity.of(Optional.empty()));
    assertThrows(NotFoundException.class, () -> planetService.save(planetRequest));
  }

  @Test
  public void shouldSavePlanet() {
    when(planetRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(starWarsAPIService.getPlanetByName(anyString()))
        .thenReturn(ResponseEntity.of(Optional.of(starWarsAPIResponse)));
    assertDoesNotThrow(() -> planetService.save(planetRequest));
  }

  @Test
  public void shouldGetAllPlanetsFromDatabase() {
    when(planetRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
        Collections.singletonList(planet)));
    Page<PlanetResponse> planets = planetService.getAllFromDatabase(Pageable.unpaged());
    assertEquals(1, planets.getContent().size());
  }

  @Test
  public void shouldGetAllPlanetsFromSwApi() {
    when(starWarsAPIService.getAllPlanets(any()))
        .thenReturn(ResponseEntity.of(Optional.of(starWarsAPIResponse)));
    Page<PlanetResponse> planets = planetService.getAllFromSwApi(1);
    assertEquals(1, planets.getContent().size());
  }

  @Test
  public void shouldCauseNotFoundExceptionWhenTryFindPlanetByName() {
    when(planetRepository.findByName(anyString()))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> planetService.findByName(PLANET_NAME));
  }

  @Test
  public void shouldFindPlanetByName() {
    when(planetRepository.findByName(anyString()))
        .thenReturn(Optional.of(planet));
    assertDoesNotThrow(() -> planetService.findByName(PLANET_NAME));
  }

  @Test
  public void shouldCauseNotFoundExceptionWhenTryFindPlanetById() {
    when(planetRepository.findById(any()))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> planetService.findById(PLANET_ID));
  }

  @Test
  public void shouldFindPlanetById() {
    when(planetRepository.findById(any()))
        .thenReturn(Optional.of(planet));
    assertDoesNotThrow(() -> planetService.findById(PLANET_ID));
  }

  @Test
  public void shouldCauseNotFoundExceptionWhenTryDeletePlanet() {
    when(planetRepository.findById(any()))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> planetService.delete(PLANET_ID));
  }

  @Test
  public void shouldDeletePlanet() {
    when(planetRepository.findById(any()))
        .thenReturn(Optional.of(planet));
    assertDoesNotThrow(() -> planetService.delete(PLANET_ID));
  }

}
