package vn.com.omart.driver.service;

public interface ThumborService {

	/**
	 * Upload Image.
	 * 
	 * @param imgName
	 * @param imageByteArray
	 * @return url
	 */
	public String uploadImage(String imgName, byte[] imageByteArray);

}
