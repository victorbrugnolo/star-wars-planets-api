package dev.victorbrugnolo.starwarsplanets.entities;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "planet")
public class Planet {

  @Id
  @Column
  @Type(type = "uuid-char")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String climate;

  @Column(nullable = false)
  private String terrain;

  @Column(nullable = false)
  private Integer numberAppearances;

}
