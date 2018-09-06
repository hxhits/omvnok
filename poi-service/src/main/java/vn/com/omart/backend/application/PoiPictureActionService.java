package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.omart.backend.domain.model.PoiPictureActionRepository;

@Service
public class PoiPictureActionService {

  @Autowired
  private PoiPictureActionRepository poiPictureActionRepository;
}
