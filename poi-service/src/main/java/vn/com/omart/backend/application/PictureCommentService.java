package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.omart.backend.application.response.PoiCommentDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.PictureComment;
import vn.com.omart.backend.domain.model.PictureCommentRepository;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PoiPictureRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

/**
 * Picture Comment Service.
 * 
 * @author Win10
 *
 */
@Service
public class PictureCommentService {

  @Autowired
  private PictureCommentRepository pictureCommentRepository;

  @Autowired
  private PoiPictureRepository poiPictureRepository;

  /**
   * Save Comment.
   * 
   * @param userId
   * @param pictureId
   * @param comment
   */
  public PictureComment saveComment(String userId, Long pictureId, PictureComment comment) {
    PictureComment pictureComment = null;
    PoiPicture picture = poiPictureRepository.findOne(pictureId);
    if (picture == null) {
      throw new NotFoundException("picture not found");
    }
    comment.setPicture(picture);
    comment.setUserId(userId);
    comment.setCreatedAt(DateUtils.getCurrentDate());
    pictureComment = pictureCommentRepository.save(comment);
    // update comment number into poi picture
    int commentNumber = pictureCommentRepository.countByPicture(picture);
    picture.setCommentNumber(commentNumber);
    poiPictureRepository.save(picture);
    return pictureComment;
  }

  /**
   * Get Comment By Picture Id.
   * 
   * @param pictureId
   * @param pageable
   * @return Page of PoiCommentDTO
   */
  public Page<PoiCommentDTO> getCommentByPictureId(Long pictureId, Pageable pageable) {
    Page<Object[]> objectPageComments = pictureCommentRepository.findCommentByPictureId(pictureId, pageable);
    Page<PoiCommentDTO> pageComments = objectPageComments.map(new PoiCommentDTO.QueryMapper()::converter);
    return pageComments;
  }

  /**
   * Get Comment By Picture Id (CreatedAt DESC).
   * 
   * @param pictureId
   * @param pageable
   * @return Page of PoiCommentDTO
   */
  public Page<PoiCommentDTO> getCommentByPictureIdDesc(Long pictureId, Pageable pageable) {
    Page<Object[]> objectPageComments = pictureCommentRepository.findCommentByPictureIdDesc(pictureId, pageable);
    Page<PoiCommentDTO> pageComments = objectPageComments.map(new PoiCommentDTO.QueryMapper()::converter);
    return pageComments;
  }

}
