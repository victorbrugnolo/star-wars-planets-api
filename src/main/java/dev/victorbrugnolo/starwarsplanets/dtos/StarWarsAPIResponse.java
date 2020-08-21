package dev.victorbrugnolo.starwarsplanets.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StarWarsAPIResponse {

  private Integer count;
  private String next;
  private List<StarWarsAPIPlanetResponse> results;

}
