package in.codifi.ambalal.rms.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RmsUpdateResponse {
	private String type;
    private String code;
    private String description;
    private Map<String, Object> result;
}
