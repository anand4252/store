package com.techopact.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private Long id;
  private String name;
  private String description;
  private double price;
  @JsonIgnore
  private int quantity;
  @JsonIgnore
  private boolean priceIncreased;

  @JsonIgnore
  @ElementCollection
  private List<LocalDateTime> lastViewedTimes;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Item item = (Item) o;
    return name.equals(item.name) &&
            Objects.equals(description, item.description) &&
            price == item.price;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, price);
  }
}
