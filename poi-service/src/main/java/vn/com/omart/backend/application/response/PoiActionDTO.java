package vn.com.omart.backend.application.response;

import vn.com.omart.backend.application.util.PoiActionStatus;

/**
 * Custom Poi Action Count.
 * 
 * @author Win10
 *
 */
public class PoiActionDTO {

	private Long poiId;
	private long count;
	private PoiActionStatus status;

	public PoiActionDTO() {
	}

	public PoiActionDTO(int actionType) {
		super();
		statusMapping(actionType);
	}

	public PoiActionDTO(long count, int actionType) {
		super();
		this.count = count;
		statusMapping(actionType);
	}

	public PoiActionDTO(long count,Long poiId, int actionType) {
		super();
		this.count = count;
		this.poiId = poiId;
		statusMapping(actionType);
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	private void statusMapping(int actionType) {
		switch (actionType) {
		case 1:
			this.status = PoiActionStatus.LIKE;
			break;
		case 2:
			this.status = PoiActionStatus.DISLIKE;
			break;
		case 3:
			this.status = PoiActionStatus.FAVORITE;
			break;
		}
	}

	public PoiActionStatus getStatus() {
		return status;
	}

	public void setStatus(PoiActionStatus status) {
		this.status = status;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}
