package dev.victorbrugnolo.starwarsplanets.resources;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.services.PlanetService;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/planets")
public class PlanetResource {

  private final PlanetService planetService;

  public PlanetResource(PlanetService planetService) {
    this.planetService = planetService;
  }

  @PostMapping
  public ResponseEntity<Void> save(@RequestBody PlanetRequest planetRequest) {
    Planet planet = planetService.save(planetRequest);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}").buildAndExpand(planet.getId())
        .toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping
  public ResponseEntity<Page<PlanetResponse>> getAllFromDatabase(Pageable pageable) {
    return ResponseEntity.ok(planetService.getAllFromDatabase(pageable));
  }

  @GetMapping("/swapi")
  public ResponseEntity<Page<PlanetResponse>> getAllFromSwApi(
      @RequestParam(required = false) Integer page) {
    return ResponseEntity.ok(planetService.getAllFromSwApi(page));
  }

}
