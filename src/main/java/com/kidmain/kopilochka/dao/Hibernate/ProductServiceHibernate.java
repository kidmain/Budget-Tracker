//package com.kidmain.kopilochka.dao.Hibernate;
//
//import com.kidmain.kopilochka.models.AppUser;
//import com.kidmain.kopilochka.models.Product;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
//@Service
//public class ProductServiceHibernate {
//    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
//
//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public ProductServiceHibernate(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional(readOnly = true)
//    public Product getProductById(Long id) {
//        Session session = sessionFactory.getCurrentSession();
//        return session.get(Product.class, id);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Product> getAllProducts() {
//        Session session = sessionFactory.getCurrentSession();
//        List<Product> products = session
//                .createQuery("SELECT p FROM Product p", Product.class)
//                .getResultList();
//        return products;
//    }
//
//    @Transactional(readOnly = true)
//    public List<Product> getAllProductsByOrder() {
//        Session session = sessionFactory.getCurrentSession();
//        List<Product> products = session
//                .createQuery("SELECT p FROM Product p ORDER BY p.id", Product.class)
//                .getResultList();
//        return products;
//    }
//
//    @Transactional
//    public void addProduct(Product product) {
//        Session session = sessionFactory.getCurrentSession();
//        session.save(product);
//    }
//
//    @Transactional
//    public void updateProduct(
//            Product oldProduct, Product updatedProduct,
//            AppUser user, AppUser updatedUser
//    ) {
//        Session session = sessionFactory.getCurrentSession();
//
//        if (oldProduct.getName() != null) updatedProduct.setName(oldProduct.getName());
//        if (oldProduct.getPrice() != null) updatedProduct.setPrice(oldProduct.getPrice());
//        if (oldProduct.getDate() != null) updatedProduct.setDate(oldProduct.getDate());
//        updatedProduct.setUser(user);
//
//        session.update(updatedProduct);
//    }
//
//    @Transactional
//    public void deleteProductById(Long id) {
//        Session session = sessionFactory.getCurrentSession();
//        session.delete(session.get(Product.class, id));
//    }
//
//    @Transactional
//    public void deleteAllProducts() {
//        Session session = sessionFactory.getCurrentSession();
//        session
//                .createQuery("DELETE FROM Product")
//                .executeUpdate();
//    }
//
//    @Transactional(readOnly = true)
//    public List<Product> getAllProductsByUserId(Long id) {
//        return null;
//    }
//}
