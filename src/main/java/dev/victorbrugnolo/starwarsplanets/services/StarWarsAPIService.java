package dev.victorbrugnolo.starwarsplanets.services;

import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class StarWarsAPIService {

  @Value("${swapi.url.planets}")
  private String swApiUrl;

  private final RestTemplate restTemplate;

  public ResponseEntity<StarWarsAPIResponse> getPlanetByName(String planetName) {
    return restTemplate
        .getForEntity(String.format("%s/?search=%s", swApiUrl, planetName),
            StarWarsAPIResponse.class);
  }

  public ResponseEntity<StarWarsAPIResponse> getAllPlanets(Integer page) {
    return restTemplate
        .getForEntity(String.format("%s/?page=%s", swApiUrl, page),
            StarWarsAPIResponse.class);
  }

}
