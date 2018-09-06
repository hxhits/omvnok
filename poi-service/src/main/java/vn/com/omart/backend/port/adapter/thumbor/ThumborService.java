package vn.com.omart.backend.port.adapter.thumbor;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.squareup.pollexor.Thumbor;
import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

@Slf4j
public class ThumborService {

  private final RestTemplate restTemplate;
  private final Environment environment;

  private Thumbor thumbor;
  private String thumborHost;
  private String thumborProxy;
  private String thumborSecretKey;

  public ThumborService(RestTemplate restTemplate, Environment environment, String thumborHost, String thumborProxy, String thumborSecretKey) {
    this.restTemplate = restTemplate;
    this.environment = environment;
    this.thumborHost = thumborHost;
    this.thumborProxy = thumborProxy;
    this.thumborSecretKey = thumborSecretKey;
    this.thumbor = Thumbor.create(thumborProxy, thumborSecretKey);
  }

  public String uploadImage(String imageName, String imageUrl) throws IOException {

    byte[] imgBytes = CommonUtils.imgUrlToBytes(imageUrl);
    String imgName = imageName;

    if (StringUtils.isEmpty(imgName)) {
      imgName = CommonUtils.getImageName(imageUrl);
    }

    String thumborImgUrl = uploadImage(imgName, imgBytes);
    return thumborImgUrl;
  }

  public String uploadImage(MultipartFile multipartImgFile) {

    String imgUrl = null;

    try {
      imgUrl = uploadImage(multipartImgFile.getOriginalFilename(), multipartImgFile.getBytes());

    } catch (IOException e) {
      throw new ApplicationException("Cannot get file from MultiPart form");
    }

    return imgUrl;
  }

  public String uploadImage(String imgName, byte[] imageByteArray) {

    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(thumborHost + "/image").build();

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setBufferRequestBody(false);
    restTemplate.setRequestFactory(requestFactory);

    HttpHeaders header = new HttpHeaders();
    header.set("Slug", imgName + ".jpg");

    ResponseEntity<String> e = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, new HttpEntity<>(imageByteArray, header), String.class);
    if (e.getStatusCode() == HttpStatus.CREATED) {
      log.debug("URL={}", e.getHeaders().getLocation());

      String originImageName = e.getHeaders().getLocation().toString().replaceAll("/image/", "");
      return thumbor.buildImage(originImageName).toUrl();
      // return originImageName;

      // String originalMetaUrl = thumborMetaUrl(thumborHost, thumborSecretKey,
      // originImageName);

      // ResponseEntity<ThumborMetaResponse> forEntity =
      // restTemplate.getForEntity(URI.create(originalMetaUrl),
      // ThumborMetaResponse.class);

      // return thumborUrl(thumborProxy, thumborSecretKey, originImageName,
      // forEntity.getBody().getThumbor().getTarget().getWidth()
      // + "x"
      // + forEntity.getBody().getThumbor().getTarget().getHeight());
    }

    throw new RuntimeException("Cannot upload image to thumbor: response_code=" + e.getStatusCode());
  }

  /*
   * TODO DELETE IMAGE FROM THUMBER SERVER
   */
  public void deleteImage1(String url1) {
    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(thumborHost + "/image").build();

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setBufferRequestBody(false);
    restTemplate.setRequestFactory(requestFactory);

    // String url =
    // "https://thumbor.omartvietnam.com/image/0d92acd7408244daa0af38c1c8528a2a/images_3.jpg.jpg";
    HttpHeaders header = new HttpHeaders();
    header.set("Slug", "images_3.jpg" + ".jpg");

    String url = uriComponents.toUri() + "/0d92acd7408244daa0af38c1c8528a2a/images_3.jpg.jpg";
    ResponseEntity<String> e = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(header), String.class);
    // restTemplate.delete(url);
  }

  public void deleteImage(String imageUrl) {
    String originImageName = imageUrl.replaceAll(this.thumborProxy, "");
  }

  // private String thumborMetaUrl(String host, String secretKey, String imageUrl)
  // {
  // String thumbnailPath = "meta/0x0/smart/" + imageUrl;
  // String sign = StringUtils.replaceChars(base64EncodeSHA1(secretKey,
  // thumbnailPath), "/+", "_-");
  // try {
  // return host + "/" + URLEncoder.encode(sign,
  // StandardCharsets.UTF_8.toString()) + "/" + thumbnailPath;
  // } catch (UnsupportedEncodingException e) {
  // e.printStackTrace();
  // throw new RuntimeException(e);
  // }
  // }

  // private String thumborUrl(String host, String secret, String imageUrl, String
  // size) {
  // String thumbnailPath = size + "/smart/" + imageUrl;
  // String sign = StringUtils.replaceChars(base64EncodeSHA1(secret,
  // thumbnailPath), "/+", "_-");
  // try {
  // return host + "/" + URLEncoder.encode(sign,
  // StandardCharsets.UTF_8.toString()) + "/" + thumbnailPath;
  // } catch (UnsupportedEncodingException e) {
  // e.printStackTrace();
  // throw new RuntimeException(e);
  // }
  // }

  // public String thumborUrl(String imageOrginal) {
  // return this.thumborProxy + "/image/" + imageOrginal;
  // }

  // private String base64EncodeSHA1(String secretKey, String thumbnailPath) {
  // Mac sha1_HMAC = null;
  // try {
  // sha1_HMAC = Mac.getInstance("HmacSHA1");
  // } catch (NoSuchAlgorithmException e) {
  // e.printStackTrace();
  // }
  // SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(),
  // "HmacSHA1");
  // try {
  // sha1_HMAC.init(secret_key);
  // } catch (InvalidKeyException e) {
  // log.error("error={}", e);
  // }
  //
  // Base64.Encoder encoder = Base64.getEncoder();
  // return encoder.encodeToString(sha1_HMAC.doFinal(thumbnailPath.getBytes()));
  // }

}
