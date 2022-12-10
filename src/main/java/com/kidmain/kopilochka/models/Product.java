package com.kidmain.kopilochka.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(
            name = "product_id_seq", sequenceName = "product_id_seq",
            allocationSize = 1, initialValue = 1)
    @Column
    private Long id;

    @Column
    @NotEmpty(message = "It should not be empty")
    private String name;

    @Column
    @PositiveOrZero(message = "It should not be negative")
    @NotNull(message = "It should not be empty")
    private Double price;

    @Column
    @NotNull(message = "It should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column
    @NotEmpty(message = "It should not be empty")
    private String username;

    public Product() {
    }

    public Product(Long id, String name, Double price, LocalDate date, String username) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.username = username;
    }

    // Hibernate constructor
    public Product(String name, Double price, LocalDate date, String username) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && name.equals(product.name)
                && price.equals(product.price)
                && date.equals(product.date)
                && username.equals(product.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, date, username);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", username='" + username + '\'' +
                '}';
    }
}