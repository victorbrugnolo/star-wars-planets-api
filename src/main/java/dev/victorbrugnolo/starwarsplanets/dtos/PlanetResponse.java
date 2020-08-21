package dev.victorbrugnolo.starwarsplanets.dtos;

import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetResponse {

  private String name;
  private String climate;
  private String terrain;
  private Integer numberAppearances;

  public static PlanetResponse domainToResponse(Planet domain) {
    return PlanetResponse.builder().name(domain.getName()).climate(domain.getClimate())
        .terrain(domain.getTerrain()).numberAppearances(domain.getNumberAppearances()).build();
  }

}
