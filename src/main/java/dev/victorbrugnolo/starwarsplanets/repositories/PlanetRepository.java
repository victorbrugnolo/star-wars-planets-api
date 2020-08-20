package dev.victorbrugnolo.starwarsplanets.repositories;

import dev.victorbrugnolo.starwarsplanets.entities.Planet;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, UUID> {

}
