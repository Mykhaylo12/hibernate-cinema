package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.RoleDao;
import com.dev.cinema.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {
    private final SessionFactory sessionFactory;

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role add(Role role) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Long id = (Long) session.save(role);
            transaction.commit();
            role.setId(id);
            return role;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't add role to DB", e);
        }
    }

    @Override
    public Role getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Role role = session.createQuery("from Role where roleName=:name", Role.class)
                    .setParameter("name", name).uniqueResult();
            return role;
        } catch (Exception e) {
            throw new RuntimeException("Can't get role by name", e);
        }
    }
}
