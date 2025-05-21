package in.codifi.ambalal.repository;

import org.springframework.data.repository.CrudRepository;

import in.codifi.ambalal.entity.PaymentTransactionEntity;

public interface PaymentTransactionRepository  extends CrudRepository<PaymentTransactionEntity,Long> {

	PaymentTransactionEntity findByClientCodeAndRazorpayPaymentId(String clientCode, String razorpayPaymentId);
	
	PaymentTransactionEntity findByClientCodeAndTxnId(String clientCode, String txnId);

}
