package vn.com.omart.backend.application;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.domain.model.Timeline;

@Service
public class StoredProcedureService {

	@Autowired
	private EntityManager em;

	public List<Timeline> getMyTimelineStoredProcedureQuery(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_user_id", params.getUserId());
		query.setParameter("orig_types", params.getTimelineTypes());
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Timeline> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Timeline> getMyTimelineStoredProcedureQuery_V3(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_user_id", params.getUserId());
		query.setParameter("orig_types", params.getTimelineTypes());
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Timeline> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Timeline> getTimelineStoredProcedureQuery(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_user_id", params.getUserId());
		query.setParameter("orig_types", params.getTimelineTypes());
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Timeline> poiObj = query.getResultList();
		return poiObj;
	}
	
	public List<Timeline> getTimelineStoredProcedureQueryV5(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_user_id", params.getUserId());
		query.setParameter("friend_str", params.getFriendStr());
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Timeline> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getPoiNotificationStoredProcedureQuery(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("categoryIds", params.getCategoryIds());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getPoiNotificationV2(String namedStoredProcedureQuery, int provinceId, String districtIds,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("categoryIds", params.getCategoryIds());
		query.setParameter("provinceId", provinceId);
		query.setParameter("districtIds", districtIds);
		query.setParameter("userId", params.getUserId());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getPoiNotificationV4(String namedStoredProcedureQuery, int provinceId, String districtIds,
			StoreProcedureParams params, String userId) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("categoryIds", params.getCategoryIds());
		query.setParameter("provinceId", provinceId);
		query.setParameter("districtIds", districtIds);
		query.setParameter("positionLevelIds", params.getPositionLevelIds());
		query.setParameter("notificationType", params.getNotificationType().getId());
		query.setParameter("userId", userId);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getPoiStoredProcedureQueryWithPoiId(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("poiIds", params.getCategoryIds());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getYourFollowPoiStoredProcedureQuery(String namedStoredProcedureQuery, int provinceId,
			String districtIds, StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("categoryIds", params.getCategoryIds());
		query.setParameter("provinceId", provinceId);
		query.setParameter("districtIds", districtIds);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	public List<Object[]> getPoiWithNoLocation(String namedStoredProcedureQuery, int provinceId, String districtIds,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("categoryIds", params.getCategoryIds());
		query.setParameter("provinceId", provinceId);
		query.setParameter("districtIds", districtIds);
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	/**
	 * Get data with latitude, longitude, radius
	 * 
	 * @param namedStoredProcedureQuery
	 * @param params
	 * @return List of array object.
	 */
	public List<Object[]> getStoredProcedureQueryWithLatLngRad(String namedStoredProcedureQuery,
			StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.execute();
		List<Object[]> objs = query.getResultList();
		return objs;
	}

	/**
	 * Filter poi by category id.
	 * 
	 * @param namedStoredProcedureQuery
	 * @param params
	 * @return List of array object
	 */
	public List<Object[]> getFilterPoiByCategory(String namedStoredProcedureQuery, StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("catId", params.getCategoryId());
		query.setParameter("provinceId", params.getProvinceId());
		query.setParameter("districtId", params.getDistrictId());
		query.setParameter("wardId", params.getWardId());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

}
