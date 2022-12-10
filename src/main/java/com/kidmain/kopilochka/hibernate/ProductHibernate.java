package com.kidmain.kopilochka.hibernate;

import com.kidmain.kopilochka.models.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductHibernate {
    // hibernate.properties
    private static final Configuration configuration = new Configuration().addAnnotatedClass(Product.class);

    public static void main(String[] args) {
        ProductHibernate hibernate = new ProductHibernate();
//        hibernate.addRandomProducts();
//        hibernate.editProductName(2L, "John");
        hibernate.deleteProduct(2L);
    }

    private Product getProductById(Long id) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try (sessionFactory) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            Product product = session.get(Product.class, id);

            session.getTransaction().commit();
            return product;
        }
    }

    private void addRandomProducts() {
        ProductHibernate hibernate = new ProductHibernate();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            products.add(
                    new Product(
                            hibernate.createRandomWord((int) (Math.random() * 50) + 1),
                            Math.random() * 10000,
                            LocalDate.of((int) (Math.random() * 2022), (int) (Math.random() * 11 + 1), (int) (Math.random() * 27 + 1)),
                            "Anna")
            );
        }

        try {
            session.beginTransaction();

            for (Product product : products) {
                session.save(product);
            }

            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    private String createRandomWord(int len) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int v = 1 + (int) (Math.random() * 26);
            char c = (char) (v + (i == 0 ? 'A' : 'a') - 1);
            word.append(c);
        }
        return word.toString();
    }

    private void editProductName(Long id, String name) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Product product = session.get(Product.class, id);
            product.setName(name);

            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    private void deleteProduct(Long id) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Product product = session.get(Product.class, id);
            session.delete(product);

            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
