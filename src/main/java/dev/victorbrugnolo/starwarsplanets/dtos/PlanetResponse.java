package dev.victorbrugnolo.starwarsplanets.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PlanetResponse {

  private String id;
  private String name;
  private String climate;
  private String terrain;
  private Integer numberAppearances;

  public static PlanetResponse domainToResponse(Planet domain) {
    return PlanetResponse.builder().id(String.valueOf(domain.getId())).name(domain.getName())
        .climate(domain.getClimate())
        .terrain(domain.getTerrain()).numberAppearances(domain.getNumberAppearances()).build();
  }

  public static PlanetResponse swApiPlanetToResponse(
      StarWarsAPIPlanetResponse swApiPlanetResponse) {
    return PlanetResponse.builder()
        .name(swApiPlanetResponse.getName())
        .climate(swApiPlanetResponse.getClimate())
        .terrain(swApiPlanetResponse.getTerrain())
        .numberAppearances(swApiPlanetResponse.getFilms().size()).build();
  }

}
