package dev.victorbrugnolo.starwarsplanets.resources;

import dev.victorbrugnolo.starwarsplanets.dtos.PlanetRequest;
import dev.victorbrugnolo.starwarsplanets.dtos.PlanetResponse;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import dev.victorbrugnolo.starwarsplanets.services.PlanetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Planets")
@RequiredArgsConstructor
@RequestMapping("/planets")
public class PlanetResource {

  private final PlanetService planetService;

  @PostMapping
  @Operation(summary = "Create new planet")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "201", description = "Planet created", content = @Content),
          @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
          @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content)
      }
  )
  public ResponseEntity<Void> save(
      @Valid
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Planet request")
      @RequestBody PlanetRequest planetRequest) {
    Planet planet = planetService.save(planetRequest);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}").buildAndExpand(planet.getId())
        .toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping
  @Operation(summary = "Obtain all planets from database")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "200", description = "Planets found", content = @Content(schema = @Schema(implementation = String.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content)
      }
  )
  public ResponseEntity<Page<PlanetResponse>> getAllFromDatabase(
      @Parameter(hidden = true) Pageable pageable) {
    return ResponseEntity.ok(planetService.getAllFromDatabase(pageable));
  }

  @GetMapping("/swapi")
  @Operation(summary = "Obtain all planets from SWAPI")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "200", description = "Planets found", content = @Content),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content),
      }
  )
  public ResponseEntity<Page<PlanetResponse>> getAllFromSwApi(
      @Parameter(description = "Number of page") @RequestParam(required = false) Integer page) {
    return ResponseEntity.ok(planetService.getAllFromSwApi(page));
  }

  @GetMapping(params = "name")
  @Operation(summary = "Obtain planet by name")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "200", description = "Planet found",
              content = @Content(schema = @Schema(implementation = PlanetResponse.class))),
          @ApiResponse(responseCode = "404", description = "Planet not found", content = @Content),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content)
      }
  )
  public ResponseEntity<PlanetResponse> findByName(
      @Parameter(description = "Planet name", required = true) @RequestParam("name") String name) {
    return ResponseEntity.ok(planetService.findByName(name));
  }

  @GetMapping("{id}")
  @Operation(summary = "Obtain planet by id")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "200",
              description = "Planet found",
              content = @Content(schema = @Schema(implementation = PlanetResponse.class))),
          @ApiResponse(responseCode = "404", description = "Planet not found", content = @Content),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content)
      }
  )
  public ResponseEntity<PlanetResponse> findById(
      @Parameter(description = "Planet id", required = true) @PathVariable("id") String id) {
    return ResponseEntity.ok(planetService.findById(id));
  }

  @DeleteMapping("{id}")
  @Operation(summary = "Delete planet")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "204", description = "Planet deleted", content = @Content),
          @ApiResponse(responseCode = "404", description = "Planet not found", content = @Content),
          @ApiResponse(responseCode = "500",
              description = "Internal server error", content = @Content)
      }
  )
  public ResponseEntity<PlanetResponse> delete(@PathVariable("id") String id) {
    planetService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
