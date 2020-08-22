package dev.victorbrugnolo.starwarsplanets.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarWarsAPIPlanetResponse {

  private String name;
  private String climate;
  private String terrain;
  private List<String> films;

}
