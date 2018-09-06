package vn.com.omart.driver.service.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.driver.common.util.CommonUtils;
import vn.com.omart.driver.dto.request.StoreProcedureParams;
import vn.com.omart.driver.service.StoredProcedureService;

@Service
public class StoredProcedureServiceImpl implements StoredProcedureService {

	@Autowired
	private EntityManager em;

	/**
	 * Get data with latitude, longitude, radius
	 * 
	 * @param namedStoredProcedureQuery
	 * @param params
	 * @return List of array object.
	 */
	@Override
	public List<Object[]> getStoredProcedureQueryWithLatLngRad(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("carTypeId", params.getCarTypeId());
		query.execute();
		List<Object[]> objs = query.getResultList();
		return objs;
	}
}
