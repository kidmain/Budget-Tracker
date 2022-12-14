//package com.kidmain.kopilochka.dao.Hibernate;
//
//import com.kidmain.kopilochka.models.AppUser;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class UserServiceHibernate {
//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public UserServiceHibernate(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional(readOnly = true)
//    public List<AppUser> getAllUsersByOrder() {
//        return null;
//    }
//}
