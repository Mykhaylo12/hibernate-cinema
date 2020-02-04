package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.MovieSessionDao;
import com.dev.cinema.lib.Dao;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.util.HibernateUtil;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieSessionDaoImpl implements MovieSessionDao {
    private static final Logger LOGGER = Logger.getLogger(MovieSessionDaoImpl.class);

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MovieSession> cq = cb.createQuery(MovieSession.class);
            Root<MovieSession> root = cq.from(MovieSession.class);
            Predicate predicatId = cb.equal(root.get("movie"), movieId);
            Predicate predicateDate = cb.between(root.get("showTime"),
                    date.atStartOfDay(), date.plusDays(1).atStartOfDay());
            cq.select(root).where(cb.and(predicatId, predicateDate));
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            LOGGER.error("Can't find Available Sessions", e);
            throw new RuntimeException();
        }
    }

    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long movieSessionId = (Long) session.save(movieSession);
            transaction.commit();
            movieSession.setId(movieSessionId);
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Can't add movie session", e);
            throw new RuntimeException();
        }
    }
}
