package dev.victorbrugnolo.starwarsplanets.dtos;

import com.sun.istack.NotNull;
import dev.victorbrugnolo.starwarsplanets.entities.Planet;
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
  private String name;

  @NotNull
  private String climate;

  @NotNull
  private String terrain;

  public static Planet toDomain(PlanetRequest planetRequest, Integer numberAppearances) {
    return Planet.builder().name(planetRequest.name).climate(planetRequest.climate)
        .terrain(planetRequest.terrain).numberAppearances(numberAppearances).build();
  }

}
