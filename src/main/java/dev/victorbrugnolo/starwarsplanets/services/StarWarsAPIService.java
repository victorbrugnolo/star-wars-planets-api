package dev.victorbrugnolo.starwarsplanets.services;

import dev.victorbrugnolo.starwarsplanets.dtos.StarWarsAPIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StarWarsAPIService {

  @Value("${swapi.url.planets}")
  private String swApiUrl;

  private final RestTemplate restTemplate;

  public StarWarsAPIService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<StarWarsAPIResponse> getPlanetByName(String planetName) {
    return restTemplate
        .getForEntity(String.format("%s/?search=%s", swApiUrl, planetName),
            StarWarsAPIResponse.class);
  }

}
