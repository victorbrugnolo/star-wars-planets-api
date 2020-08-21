package dev.victorbrugnolo.starwarsplanets.services;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.exceptions.NotFoundException;
import dev.victorbrugnolo.starwarsplanets.repositories.PlanetRepository;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

  private final PlanetRepository planetRepository;
  private final StarWarsAPIService starWarsAPIService;

  private final static String ERR_MSG_PLANET_NOT_FOUND_SWAPI = "Planet %s not found in SWAPI";

  public PlanetService(
      PlanetRepository planetRepository,
      StarWarsAPIService starWarsAPIService) {
    this.planetRepository = planetRepository;
    this.starWarsAPIService = starWarsAPIService;
  }

  public Planet save(PlanetRequest planetRequest) {
    ResponseEntity<StarWarsAPIResponse> planetInSwApi = starWarsAPIService
        .getPlanetByName(planetRequest.getName());

    if (Objects.isNull(planetInSwApi.getBody()) || planetInSwApi.getBody().getResults().isEmpty()) {
      throw new NotFoundException(
          String.format(ERR_MSG_PLANET_NOT_FOUND_SWAPI, planetRequest.getName()));
    }

    return planetRepository.save(PlanetRequest.toDomain(planetRequest, Objects
        .requireNonNull(planetInSwApi.getBody()).getResults().get(0).getFilms().size()));
  }

}
