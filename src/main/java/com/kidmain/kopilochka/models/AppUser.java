package com.kidmain.kopilochka.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private Double income = 0.0;

    @Column
    private Double expenses = 0.0;

    @Column
    private Double debt = 0.0;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    public AppUser() {
    }

    public AppUser(Long id, String name, Double income, Double expenses, Double debt, List<Product> products) {
        this.id = id;
        this.name = name;
        this.income = income;
        this.expenses = expenses;
        this.debt = debt;
        this.products = products;
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

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Double.compare(appUser.income, income) == 0 && Double.compare(appUser.expenses, expenses) == 0 && Double.compare(appUser.debt, debt) == 0 && Objects.equals(id, appUser.id) && Objects.equals(name, appUser.name) && Objects.equals(products, appUser.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, income, expenses, debt, products);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
