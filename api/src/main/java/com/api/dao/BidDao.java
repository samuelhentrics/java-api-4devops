package com.api.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import com.api.data.entity.Bid;
import com.api.exception.business.ResourceStateConflictException;
import com.api.exception.technical.DAOException;
import com.api.jpa.bootstrap.config.JpaEntityManager;

@Stateless
public class BidDao {
	
	public List<Bid> getAll() throws DAOException{
		EntityManager entityManager = null;
		try {
			entityManager = JpaEntityManager.getEntityManagerFactory().createEntityManager();
			//entityManager.getTransaction().begin();
			//entityManager.flush();
			return entityManager.createQuery("SELECT p FROM Bid p", Bid.class).getResultList();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();				
			}
		}
	}
	
	public Bid get(long id) throws DAOException {
		EntityManager entityManager = null;
		try {
			entityManager = JpaEntityManager.getEntityManagerFactory().createEntityManager();
			return entityManager.find(Bid.class, id);
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	public void create(Bid entity) throws DAOException {
		EntityManager entityManager = null;
		try {
			entityManager = JpaEntityManager.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	public void update(Bid entity) throws DAOException {
		EntityManager entityManager = null;
		try {
			entityManager = JpaEntityManager.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	public void delete(Bid entity) throws DAOException, ResourceStateConflictException {
		EntityManager entityManager = null;
		try {
			entityManager = JpaEntityManager.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();
			entity = entityManager.merge(entity);
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		}
		catch (RollbackException e) {
			throw new ResourceStateConflictException(e);
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}
