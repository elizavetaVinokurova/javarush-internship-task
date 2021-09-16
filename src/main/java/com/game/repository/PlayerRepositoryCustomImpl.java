package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public List<Player> findAllWithFilters(String name, String title, Race race, Profession profession, Boolean banned,
                                           Long after, Long before, Integer minExperience, Integer maxExperience,
                                           Integer minLevel, Integer maxLevel,
                                           PlayerOrder order, Integer pageNumber, Integer pageSize) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Player> cq=cb.createQuery(Player.class);
        Root<Player> player = cq.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(
                    cb.like(player.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(
                    cb.like(player.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(
                    cb.equal(player.get("race"), race));
        }
        if (profession != null) {
            predicates.add(
                    cb.equal(player.get("profession"), profession));
        }
        if (banned != null) {
            predicates.add(
                    cb.equal(player.get("banned"), banned));
        }
        if (after != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(player.get("birthday"), new Date(after)));
        }
        if (before != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(player.get("birthday"), new Date(before)));
        }
        if (minExperience != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(player.get("experience"), minExperience));
        }
        if (maxExperience != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(player.get("experience"), maxExperience));
        }
        if (minLevel != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(player.get("level"), minLevel));
        }
        if (maxLevel != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(player.get("level"), maxLevel));
        }

        cq.select(player)
                .where(predicates.toArray(new Predicate[]{}));

        cq.orderBy(cb.asc(player.get(order.getFieldName())));
        TypedQuery<Player> query = em.createQuery(cq);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Player> players = query.getResultList();

        em.getTransaction().commit();
        em.close();

        return players;
    }

    @Override
    public long getPlayersCount(String name, String title, Race race, Profession profession, Boolean banned,
                                Long after, Long before, Integer minExperience, Integer maxExperience,
                                Integer minLevel, Integer maxLevel) {
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Player> countRoot = countQuery.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(
                    cb.like(countRoot.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(
                    cb.like(countRoot.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(
                    cb.equal(countRoot.get("race"), race));
        }
        if (profession != null) {
            predicates.add(
                    cb.equal(countRoot.get("profession"), profession));
        }
        if (banned != null) {
            predicates.add(
                    cb.equal(countRoot.get("banned"), banned));
        }
        if (after != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(countRoot.get("birthday"), new Date(after)));
        }
        if (before != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(countRoot.get("birthday"), new Date(before)));
        }
        if (minExperience != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(countRoot.get("experience"), minExperience));
        }
        if (maxExperience != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(countRoot.get("experience"), maxExperience));
        }
        if (minLevel != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(countRoot.get("level"), minLevel));
        }
        if (maxLevel != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(countRoot.get("level"), maxLevel));
        }

        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(countQuery).getSingleResult();
    }
}
