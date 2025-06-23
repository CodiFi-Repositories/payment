package in.codifi.ambalal.rms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RmsUpdateRequest {

	   private String LoginId;
	    private String ClientId;
	    private Double PayInAmout;
	    private Double AdhocAll;
}
