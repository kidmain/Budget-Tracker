package com.kidmain.kopilochka.dao.Hibernate;

import com.kidmain.kopilochka.models.AppUser;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class AppUserServiceHibernate {
    private final EntityManager entityManager;

    @Autowired
    public AppUserServiceHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AppUser getUserById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(AppUser.class, id);
    }
}
