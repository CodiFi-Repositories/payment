package in.codifi.ambalal.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequestModel {
    private List<String> mailIds;
    private String subject;
    private String message;
}
