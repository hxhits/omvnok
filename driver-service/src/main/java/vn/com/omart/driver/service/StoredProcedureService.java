package vn.com.omart.driver.service;

import java.util.List;

import vn.com.omart.driver.dto.request.StoreProcedureParams;

public interface StoredProcedureService {
	
	public List<Object[]> getStoredProcedureQueryWithLatLngRad(String namedStoredProcedureQuery,
			StoreProcedureParams params);
}
