package dev.victorbrugnolo.starwarsplanets.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StarWarsAPIResponse {

  private Integer count;
  private String next;
  private List<StarWarsAPIPlanetResponse> results;

}
