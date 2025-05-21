package com.metaltech.dao;

import com.metaltech.config.JPAConfig;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class BaseDAO<T> {
    protected EntityManager em;
    protected Class<T> entityClass;

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.em = JPAConfig.getEntityManager();
    }

    public void beginTransaction() {
        em.getTransaction().begin();
    }

    public void commit() {
        em.getTransaction().commit();
    }

    public void rollback() {
        em.getTransaction().rollback();
    }

    public void close() {
        JPAConfig.closeEntityManager();
    }

    public T save(T entity) {
        beginTransaction();
        em.persist(entity);
        commit();
        return entity;
    }

    public T update(T entity) {
        beginTransaction();
        T updatedEntity = em.merge(entity);
        commit();
        return updatedEntity;
    }

    public void delete(Long id) {
        beginTransaction();
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
        commit();
    }

    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e").getResultList();
    }
} 