package in.codifi.ambalal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_transaction")
public class PaymentTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "transaction_id")
    private String txnId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private String status;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "atom_txn_id")
    private String atomTxnId;

    @Column(name = "discriminator")
    private String discriminator;

    @Column(name = "surcharge")
    private String surcharge;

    @Column(name = "card_number")
    private String cardNumber;

    
    private String txnDate;

    @Column(name = "customer_account_no")
    private String customerAccNo;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "is_atom")
    private Boolean isAtom;

    @Column(name = "is_razorpay")
    private Boolean isRazorpay;

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "razorpay_signature")
    private String razorpaySignature;

    @Column(name = "amount_paid")
    private Integer amountPaid;

    @Column(name = "amount_due")
    private Integer amountDue;

    // Getters and setters (or use Lombok @Data/@Getter/@Setter for brevity)
}
