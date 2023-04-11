package com.laby.projektkrypto.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.laby.projektkrypto.entity.Event;

@Repository
public class EventRepositoryCustom
{
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String COIN = "coin";
    private static final String CATEGORY = "category";
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Event> findEvents(FindEventRequest request)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        addDatePredicates(request, cb, root, predicates);
        if (request.category() != null)
        {
            predicates.add(cb.equal(root.get(CATEGORY), request.category()));
        }
        if (request.coin() != null)
        {
            predicates.add(cb.equal(root.get(COIN), request.coin()));
        }

        cr.select(root).where(
                cb.and(predicates.toArray(Predicate[]::new))
        );
        cr.orderBy(cb.asc(root.get(START_DATE)));

        TypedQuery<Event> query = entityManager.createQuery(cr);
        return query.getResultList();
    }

    private void addDatePredicates(FindEventRequest request, CriteriaBuilder cb, Root<Event> root, List<Predicate> predicates)
    {
        if (request.startDate() != null && request.endDate() != null)
        {
            predicates.add(
                    cb.and(
                            cb.or(
                                    cb.greaterThanOrEqualTo(root.get(START_DATE), request.startDate()),
                                    cb.greaterThanOrEqualTo(root.get(END_DATE), request.startDate())
                            ),
                            cb.or(
                                    cb.lessThanOrEqualTo(root.get(START_DATE), request.endDate()),
                                    cb.lessThanOrEqualTo(root.get(END_DATE), request.endDate())
                            )
                    )
            );
        }
        else if (request.startDate() != null)
        {
            predicates.add(cb.or(
                    cb.greaterThanOrEqualTo(root.get(START_DATE), request.startDate()),
                    cb.greaterThanOrEqualTo(root.get(END_DATE), request.startDate())
            ));
        }
        else if (request.endDate() != null)
        {
            predicates.add(cb.or(
                    cb.lessThanOrEqualTo(root.get(START_DATE), request.endDate()),
                    cb.lessThanOrEqualTo(root.get(END_DATE), request.endDate())
            ));
        }
    }
}