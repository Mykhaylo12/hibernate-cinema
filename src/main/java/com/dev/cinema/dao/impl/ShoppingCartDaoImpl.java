package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.ShoppingCartDao;
import com.dev.cinema.lib.Dao;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long cartId = (Long) session.save(shoppingCart);
            transaction.commit();
            shoppingCart.setId(cartId);
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cant add shopping cart to DB ", e);
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ShoppingCart shoppingCart =
                    session.createQuery("FROM ShoppingCart WHERE user=:user", ShoppingCart.class)
                            .setParameter("user", user).uniqueResult();
            List<Ticket> tickets = session.createQuery("SELECT ticket FROM ShoppingCart AS sc"
                    + " JOIN sc.tickets AS ticket WHERE sc.user.id=:userId", Ticket.class)
                    .setParameter("userId", user.getId()).list();
            shoppingCart.setTickets(tickets);
            return shoppingCart;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't get user by email", e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.update(shoppingCart);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                throw new RuntimeException("Can't update shopping cat in DB", e);
            }
        }
    }
}
