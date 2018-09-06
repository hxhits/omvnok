package vn.com.omart.backend.application.response;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import vn.com.omart.backend.domain.model.PictureComment;

@Data
public class PictureCommentDTO {

	private Long id;
	private String userId;
	private String comment;
	private UserDTO user;

	@JsonIgnore
	public static Set<String> userIds = new HashSet<String>();

	public static PictureCommentDTO from(PictureComment entity) {
		PictureCommentDTO dto = new PictureCommentDTO();

		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setComment(entity.getComment());

		userIds.add(entity.getUserId());

		return dto;

	}
}
