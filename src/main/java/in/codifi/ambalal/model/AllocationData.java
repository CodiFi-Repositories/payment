package in.codifi.ambalal.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocationData {
	private String msgId;
    private String requestType;
    private List<AllocationRequest> allocationRequest;
}
