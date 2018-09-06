package vn.com.omart.backend.port.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.com.omart.backend.application.PictureCommentService;
import vn.com.omart.backend.application.PoiPictureService;
import vn.com.omart.backend.application.response.PoiCommentDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.PictureComment;

/**
 * Picture Comment Controller.
 * 
 * @author Win10
 *
 */
@RestController
@RequestMapping("/v1/pois")
public class PictureCommentResource {

  @Autowired
  private PoiPictureService poiPictureService;

  @Autowired
  private PictureCommentService pictureCommentService;

  /**
   * Save Picture Comment.
   * 
   * @param pictureId
   * @param comment
   * @param userId
   * @return EmptyJsonResponse
   */
  @RequestMapping(value = "/picture/{picture-id}/comment", method = RequestMethod.POST)
  public ResponseEntity<EmptyJsonResponse> saveComment(@PathVariable(value = "picture-id", required = true) Long pictureId,
      @RequestBody(required = true) PictureComment comment, @RequestHeader(value = "X-User-Id", required = true) String userId) {
    pictureCommentService.saveComment(userId, pictureId, comment);
    return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
  }

  /**
   * Get Picture Comments.
   * 
   * @param pictureId
   * @param pageable
   * @return Page of PoiCommentDTO
   */
  @RequestMapping(value = "/picture/{picture-id}/comments", method = RequestMethod.GET)
  public ResponseEntity<Page<PoiCommentDTO>> getComments(@PathVariable(value = "picture-id", required = true) Long pictureId,
      @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
    Page<PoiCommentDTO> comments = pictureCommentService.getCommentByPictureId(pictureId, pageable);
    if (null == comments) {
      return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.OK);
  }

  /**
   * Get Picture Comments (CreatedAt DESC).
   * 
   * @param pictureId
   * @param pageable
   * @return Page of PoiCommentDTO
   */
  @RequestMapping(value = "/picture/{picture-id}/comments/desc", method = RequestMethod.GET)
  public ResponseEntity<Page<PoiCommentDTO>> getCommentsDesc(@PathVariable(value = "picture-id", required = true) Long pictureId,
      @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
    Page<PoiCommentDTO> comments = pictureCommentService.getCommentByPictureIdDesc(pictureId, pageable);
    if (null == comments) {
      return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.OK);
  }

  /**
   * Share Picture to facebook.
   * 
   * @param poiId
   * @return HTML
   */

  @RequestMapping(value = "/{poi-id}/picture/{picture-id}/facebook", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> facebookShare(@PathVariable(value = "poi-id", required = true) Long poiId,
      @PathVariable(value = "picture-id", required = true) Long pictureId) {
    String html = poiPictureService.getSharePicture(poiId, pictureId);
    return new ResponseEntity<String>(html, HttpStatus.OK);
  }

}
