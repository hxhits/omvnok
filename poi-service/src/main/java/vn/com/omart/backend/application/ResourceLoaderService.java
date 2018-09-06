package vn.com.omart.backend.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * Resource Loader Service.
 * 
 * @author Win10
 *
 */
@Service
public class ResourceLoaderService {

  @Autowired
  private ResourceLoader resourceLoader;

  /**
   * Get resource.
   * 
   * @param path
   * @return HTML String
   */
  public String getResource(String path) {
    String html = "";
    Resource resurce = resourceLoader.getResource("classpath:" + path);
    InputStream is;
    try {
      is = resurce.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = br.readLine()) != null) {
        html = html + line;
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return html;
  }
}
