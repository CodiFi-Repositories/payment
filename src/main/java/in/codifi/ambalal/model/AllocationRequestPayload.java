package in.codifi.ambalal.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocationRequestPayload {

	private String AccessToken;
	private String Companytype;
	private String Clrtype;
	private String DataFormat;
	private String DataTotalCount;
	private AllocationData data;
}
