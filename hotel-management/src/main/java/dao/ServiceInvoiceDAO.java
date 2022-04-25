package dao;

import db.DBConnectionException;
import models.ServiceInvoice;

import java.util.ArrayList;
import java.util.Optional;

public class ServiceInvoiceDAO implements DAO<ServiceInvoice, Integer> {

	@Override
	public ArrayList<ServiceInvoice> getAll() throws DBConnectionException {
		return null;
	}

	@Override
	public Optional<ServiceInvoice> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(ServiceInvoice entity) throws DBConnectionException {

	}

	@Override
	public void update(ServiceInvoice entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
