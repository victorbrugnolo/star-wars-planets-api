package dev.victorbrugnolo.starwarsplanets.resources;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.services.PlanetService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/planets")
@RequiredArgsConstructor
public class PlanetResource {

  private final PlanetService planetService;

  @PostMapping
  public ResponseEntity<Void> save(@Valid @RequestBody PlanetRequest planetRequest) {
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

  @GetMapping(params = "name")
  public ResponseEntity<PlanetResponse> findByName(@RequestParam("name") String name) {
    return ResponseEntity.ok(planetService.findByName(name));
  }

  @GetMapping("{id}")
  public ResponseEntity<PlanetResponse> findById(@PathVariable("id") String id) {
    return ResponseEntity.ok(planetService.findById(id));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<PlanetResponse> delete(@PathVariable("id") String id) {
    planetService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
