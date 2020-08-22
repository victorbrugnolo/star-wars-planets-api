package dev.victorbrugnolo.starwarsplanets.services;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.exceptions.InternalServerErrorException;
import dev.victorbrugnolo.starwarsplanets.exceptions.NotFoundException;
import dev.victorbrugnolo.starwarsplanets.repositories.PlanetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class PlanetService {

  private final PlanetRepository planetRepository;
  private final StarWarsAPIService starWarsAPIService;

  private final static String ERR_MSG_PLANET_NOT_FOUND_SWAPI = "Planet %s not found in SWAPI";
  private final static Integer NUMBER_OF_ELEMENTS_PER_PAGE_SWAPI = 10;
  private final static Integer NUMBER_OF_ELEMENTS_TOTAL_FOR_EXCEPTION_SWAPI = 60;

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

  public Page<PlanetResponse> getAllFromDatabase(Pageable pageable) {
    return planetRepository.findAll(pageable).map(PlanetResponse::domainToResponse);
  }

  public Page<PlanetResponse> getAllFromSwApi(Integer page) {
    int pageToApiSearch = Objects.nonNull(page) && page > 0 ? page : 1;

    try {
      ResponseEntity<StarWarsAPIResponse> apiResponse = starWarsAPIService
          .getAllPlanets(pageToApiSearch);

      int totalPlanetsInApi = Objects.requireNonNull(apiResponse.getBody()).getCount();

      List<PlanetResponse> planetResponse =
          Objects.nonNull(apiResponse.getBody()) ? Objects
              .requireNonNull(apiResponse.getBody()).getResults().stream()
              .map(PlanetResponse::swApiPlanetToResponse)
              .collect(Collectors.toList()) : new ArrayList<>();

      return new PageImpl<>(planetResponse,
          PageRequest.of(pageToApiSearch, NUMBER_OF_ELEMENTS_PER_PAGE_SWAPI),
          totalPlanetsInApi);
    } catch (HttpClientErrorException ex) {
      if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new PageImpl<>(new ArrayList<>(),
            PageRequest.of(pageToApiSearch, NUMBER_OF_ELEMENTS_PER_PAGE_SWAPI),
            NUMBER_OF_ELEMENTS_TOTAL_FOR_EXCEPTION_SWAPI);
      }

      throw new InternalServerErrorException(ex.getMessage());
    }
  }

}
