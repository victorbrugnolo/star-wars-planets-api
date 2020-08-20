package dev.victorbrugnolo.starwarsplanets.resources;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.services.PlanetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planets")
public class PlanetResource {

  private final PlanetService planetService;

  public PlanetResource(PlanetService planetService) {
    this.planetService = planetService;
  }

  @PostMapping
  public void save(@RequestBody PlanetRequest planetRequest) {
    planetService.save(planetRequest);
  }

}
