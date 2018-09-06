package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.constants.OmartType.InterviewResult;

@Data
public class UserCvInterviewResultDTO {
	private InterviewResult result;
}
