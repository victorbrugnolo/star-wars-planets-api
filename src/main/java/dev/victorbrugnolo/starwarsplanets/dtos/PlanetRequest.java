package dev.victorbrugnolo.starwarsplanets.dtos;

import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetRequest {

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  @NotEmpty
  private String climate;

  @NotNull
  @NotEmpty
  private String terrain;

  public static Planet toDomain(PlanetRequest planetRequest, Integer numberAppearances) {
    return Planet.builder().name(planetRequest.name).climate(planetRequest.climate)
        .terrain(planetRequest.terrain).numberAppearances(numberAppearances).build();
  }

}
