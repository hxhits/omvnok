package vn.com.omart.backend.application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.MagazineDTO;
import vn.com.omart.backend.application.response.RssDTO;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.domain.model.Magazine;
import vn.com.omart.backend.domain.model.MagazineRepository;

@Service
public class MagazineService {

	@Autowired
	private MagazineRepository magazineRepository;

	/**
	 * Create magazine.
	 * 
	 * @param userId
	 * @param dto
	 * @param device
	 *        default mobile platform
	 * @return
	 */
	public MagazineDTO createMagazine(String userId, MagazineDTO dto, Device device) {
		dto.setUserId(userId);
		Magazine entity = MagazineDTO.toEntity(dto);
		entity.setCreatedAt(new Date());
		entity.setHash(String.valueOf(entity.getLink().hashCode()));
		entity = magazineRepository.save(entity);
		return MagazineDTO.from(entity);
	}

	/**
	 * Get Magazine.
	 * 
	 * @param userId
	 * @param catId
	 * @param pageable
	 * @return
	 */
	public List<MagazineDTO> getMagazine(String userId, Long catId, Pageable pageable) {
		// Get all Magazine
		List<Magazine> results = null;
		if (catId < 0) {
			results = magazineRepository.findAllByOrderByTimestampDesc(pageable);
		} else {
			results = magazineRepository.findAllByCatIdOrderByTimestampDesc(catId, pageable);
		}
		List<MagazineDTO> magazineDTO = null;
		magazineDTO = results.stream().map(MagazineDTO::from).collect(Collectors.toList());
		return magazineDTO;
	}

	/**
	 * Get RSS
	 * @param userId
	 * @param request
	 * @param device
	 * @return
	 */
	public RssDTO getRSS(String userId, RssDTO request, Device device) {
		String xml;
		System.out.println("Begin...");
		try {
			xml = getRequest(request.getUrl());
			System.out.println(xml);
		} catch (IOException e) {
			e.printStackTrace();
			xml = e.getMessage();
		}
		request.setXml(xml);
		System.out.println("End...");
		System.out.println(xml);
		return request;
	}

	/**
	 * getRequest
	 * @param reqUrl
	 * @throws IOException
	 */
    public String getRequest(String reqUrl) throws IOException {
    	URL url = new URL(reqUrl);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	
    	Map<String, String> parameters = new HashMap<>();
    	parameters.put("param1", "val");
    	 
    	con.setDoOutput(true);
    	DataOutputStream out = new DataOutputStream(con.getOutputStream());
    	out.writeBytes(getParamsString(parameters));
    	out.flush();
    	out.close();
    	
    	int status = con.getResponseCode();
    	BufferedReader in = new BufferedReader(
		  new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();
		con.disconnect();
		return content.toString();
    }
    
    private String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
}
