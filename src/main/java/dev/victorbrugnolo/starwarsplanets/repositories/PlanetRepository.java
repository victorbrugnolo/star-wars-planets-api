package dev.victorbrugnolo.starwarsplanets.repositories;

import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, UUID> {

  Optional<Planet> findByName(String name);

}
