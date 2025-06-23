package in.codifi.ambalal.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusInquiryResponse {

    private String status;
    private String messagesCode;
    private String messages;
    private InquiryData data;
}
