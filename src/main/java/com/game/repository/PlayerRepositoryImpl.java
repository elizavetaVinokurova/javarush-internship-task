package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRepositoryImpl implements PlayerRepositoryCustom {

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public List<Player> getPlayersByNameAndTitle(String name, String title) {
        EntityManager em = this.emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Player> root = cq.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(
                    cb.like(root.get("name"), name));
        }
        if (title != null) {
            predicates.add(
                    cb.like(root.get("title"), title));
        }

        cq.select(root)
                .where(predicates.toArray(new Predicate[]{}));

        return em.createQuery(cq).getResultList();
    }
}
