package com.kidmain.kopilochka.dao.Hibernate;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class ProductServiceHibernate {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private final EntityManager entityManager;

    @Autowired
    public ProductServiceHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Product.class, id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        Session session = entityManager.unwrap(Session.class);
        List<Product> products = session
                .createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.user", Product.class)
                .getResultList();
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsByOrder() {
        Session session = entityManager.unwrap(Session.class);
        List<Product> products = session
                .createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.user ORDER BY p.id", Product.class)
                .getResultList();
        return products;
    }

    @Transactional
    public void addProduct(Product product) {
        Session session = entityManager.unwrap(Session.class);
        session.save(product);
    }

    @Transactional
    public void updateProduct(
            Product oldProduct, Product updatedProduct,
            AppUser user, AppUser updatedUser
    ) {
        Session session = entityManager.unwrap(Session.class);

        updatedProduct.setName(oldProduct.getName());
        updatedProduct.setPrice(oldProduct.getPrice());
        updatedProduct.setDate(oldProduct.getDate());
        updatedProduct.setUser(user);

        session.update(updatedProduct);
    }

    @Transactional
    public void deleteProductById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(session.get(Product.class, id));
    }

    @Transactional
    public void deleteAllProducts() {
        Session session = entityManager.unwrap(Session.class);
        session
                .createQuery("DELETE FROM Product")
                .executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsByUserId(Long id) {
        return null;
    }

    /////////////////////////////////////////
    /// SPECIFIC HIBERNATE CONFIGURATION ///
    ///////////////////////////////////////
//    private final EntityManager entityManager;
//    
//    public ProductServiceHibernate(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//    @Transactional(readOnly = true)
//    public List<Product> getAllProductsByOrder() {
//        Session session = entityManager.unwrap(Session.class);
//        List<Product> products = session
//                .createQuery("SELECT p FROM Product p ORDER BY p.id", Product.class)
//                .getResultList();
//        return products;
//    }
}
