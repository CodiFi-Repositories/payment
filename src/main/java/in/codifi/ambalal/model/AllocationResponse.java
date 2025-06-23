package in.codifi.ambalal.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocationResponse {

	private String status;
	private String messagesCode;
	private String messages;
	private AllocationResponseData data;
}
