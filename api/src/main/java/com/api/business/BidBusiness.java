package com.api.business;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.api.dao.BidDao;
import com.api.data.entity.Bid;
import com.api.exception.business.NotFoundException;
import com.api.exception.business.ResourceStateConflictException;
import com.api.exception.technical.DAOException;

@Stateless
public class BidBusiness {
	@Inject
	private BidDao BidDao;
	
	public List<Bid> getAll() throws DAOException{
		return BidDao.getAll();
	}
	
	public Bid get(long id) throws DAOException, NotFoundException {
		Bid entity = BidDao.get(id);
		if (!Objects.isNull(entity)) {
			 return entity; 
		 } else {
			 throw new NotFoundException("The Bid with id " + id + " does not exist");
		 }
	}
	
	public Bid add(Bid entity) throws DAOException, NotFoundException {
		BidDao.create(entity);
		return BidDao.get(entity.getId());
	}
	
	public Bid update(long oldEntityId, Bid entity) throws DAOException, NotFoundException {
		Bid oldEntity = get(oldEntityId);
		oldEntity.setBid(entity.getBid());
		oldEntity.setMessage(entity.getMessage());
		oldEntity.setDateUpdate(new Date());
		BidDao.update(oldEntity);
		return BidDao.get(oldEntityId);
	}
	
	public void delete(long id) throws DAOException, ResourceStateConflictException {
		Bid entity = BidDao.get(id);
		BidDao.delete(entity);
	}
}
