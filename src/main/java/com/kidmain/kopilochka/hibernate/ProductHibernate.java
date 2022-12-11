package com.kidmain.kopilochka.hibernate;

import com.kidmain.kopilochka.models.AppUser;
import com.kidmain.kopilochka.models.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////
 * USELESS GARBAGE CLASS FOR LEARNING HIBERNATE
 * ////////////////////////////////////////////
 */

@Service
public class ProductHibernate {
    // hibernate.properties
    private static final Configuration configuration = new Configuration()
            .addAnnotatedClass(Product.class)
            .addAnnotatedClass(AppUser.class);

    public static void main(String[] args) {
//        addProduct();
        addRandomProducts();
//        editProductName(1L, "UPDATED");
//        deleteProduct(2L);
//        HQL(false);
    }

    private static Product getProductById(Long id) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try (sessionFactory) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            Product product = session.get(Product.class, id);

            session.getTransaction().commit();
            return product;
        }
    }

    private static void addProduct() {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try (sessionFactory) {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            AppUser user = session.get(AppUser.class, 1L);
            Product product = new Product(
                    createRandomWord((int) (Math.random() * 50) + 1),
                    Math.random() * 10000,
                    LocalDate.of((int) (Math.random() * 2022), (int) (Math.random() * 11 + 1), (int) (Math.random() * 27 + 1)),
                    user);
            // It's not mandatory to add a product for the user.
            // Hibernate may display an old version of the user due to caching.
            user.getProducts().add(product);

            session.save(product);
            session.getTransaction().commit();
        }
    }

    private static void addRandomProducts() {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            AppUser user = null;
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                user = session.get(AppUser.class, (long) (Math.random() * 2) + 1);
                products.add(
                        new Product(
                                createRandomWord((int) (Math.random() * 50) + 1),
                                Double.parseDouble(String.format("%.2f", (Math.random() * 10000 + 1))),
                                LocalDate.of((int) (Math.random() * 2022), (int) (Math.random() * 12 + 1), (int) (Math.random() * 28 + 1)),
                                user)
                );
            }

            for (Product product : products) {
                // It's not mandatory to add a product for the user.
                // Hibernate may display an old version of the user due to caching.
                user.getProducts().add(product);
                session.save(product);
            }

            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    private static String createRandomWord(int len) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int v = 1 + (int) (Math.random() * 26);
            char c = (char) (v + (i == 0 ? 'A' : 'a') - 1);
            word.append(c);
        }
        return word.toString();
    }

    private static void editProductName(Long id, String name) {
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

    private static void deleteProduct(Long id) {
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

    private static void HQL(boolean isUpdate) {
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            List<Product> products = new ArrayList<>();
            session
                    .createQuery("FROM Product WHERE user.id = 1 ")
                    .getResultStream()
                    .forEach( product -> products.add((Product) product) );

            if (isUpdate) {
                session
                        .createQuery("UPDATE Product SET name='UPDATED'")
                        .executeUpdate();
            }

            session.getTransaction().commit();

            products.forEach(System.out::println);
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
