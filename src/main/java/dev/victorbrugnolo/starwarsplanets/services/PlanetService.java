package dev.victorbrugnolo.starwarsplanets.services;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import dev.victorbrugnolo.starwarsplanets.repositories.PlanetRepository;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

  private final PlanetRepository planetRepository;
  private final StarWarsAPIService starWarsAPIService;

  public PlanetService(
      PlanetRepository planetRepository,
      StarWarsAPIService starWarsAPIService) {
    this.planetRepository = planetRepository;
    this.starWarsAPIService = starWarsAPIService;
  }

  public void save(PlanetRequest planetRequest) {
    ResponseEntity<StarWarsAPIResponse> planetInSwApi = starWarsAPIService
        .getPlanetByName(planetRequest.getName());
    planetRepository.save(PlanetRequest.toDomain(planetRequest, Objects
        .requireNonNull(planetInSwApi.getBody()).getResults().get(0).getFilms().size()));
  }

}
