package com.masa.appointment.service_catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "durationMinutes is required")
    @Min(value = 1, message = "durationMinutes must be >= 1")
    @Column(nullable = false)
    private Integer durationMinutes;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be >= 0")
    @Column(nullable = false)
    private Double price;

    public ServiceEntity() {}

    public ServiceEntity(String name, Integer durationMinutes, Double price) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
