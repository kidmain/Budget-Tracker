package com.kidmain.kopilochka.repositories;

import com.kidmain.kopilochka.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
//@Transactional(readOnly = true)
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByOrderById();
    Optional<List<Product>> findByUserId(Long id);
}