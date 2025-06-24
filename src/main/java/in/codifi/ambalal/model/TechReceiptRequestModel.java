package in.codifi.ambalal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechReceiptRequestModel {

    @JsonProperty("VoucherDate")
    private String voucherDate;

    @JsonProperty("AccountCode")
    private String accountCode;

    @JsonProperty("COMPANYCODE")
    private String companyCode;

    @JsonProperty("cheque_no")
    private String chequeNo;

    @JsonProperty("Amount")
    private Double amount;

    @JsonProperty("PostingBankAccount")
    private String postingBankAccount;

    @JsonProperty("BankAccountNumber")
    private String bankAccountNumber;

    @JsonProperty("NARRATION")
    private String narration;

    @JsonProperty("ENTRYTYPE")
    private String entryType;

    @JsonProperty("Mode")
    private String mode;

    @JsonProperty("ActualTime")
    private String actualTime;

    // Add other fields and getter/setters
}
