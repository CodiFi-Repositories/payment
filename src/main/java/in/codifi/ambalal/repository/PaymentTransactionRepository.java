package in.codifi.ambalal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.ambalal.entity.PaymentTransactionEntity;

public interface PaymentTransactionRepository  extends CrudRepository<PaymentTransactionEntity,Long> {

	PaymentTransactionEntity findByClientCodeAndRazorpayPaymentId(String clientCode, String razorpayPaymentId);
	
	PaymentTransactionEntity findByClientCodeAndTxnId(String clientCode, String atomTxnId);
	
	// Search for Atom entries
	List<PaymentTransactionEntity> findByTxnDateContaining(String date);

	// Search for Razorpay entries
	List<PaymentTransactionEntity> findByRazorpayCreatedAtEpochContaining(String date);
	

	
//	@Query("SELECT SUM(COALESCE(p.amountPaid, 0) + COALESCE(p.amount, 0)) " +
//		       "FROM PaymentTransactionEntity p " +
//		       "WHERE p.clientCode = :clientCode AND " +
//		       "(SUBSTRING(p.txnDate, 1, 10) = :dateStr OR SUBSTRING(p.razorpayCreatedAtEpoch, 1, 10) = :dateStr)")
//		Double sumAmountByClientAndDate(@Param("clientCode") String clientCode,
//		                                @Param("dateStr") String dateStr);

	@Query("SELECT SUM(COALESCE(p.amountPaid, 0) + COALESCE(p.amount, 0)) " +
		       "FROM PaymentTransactionEntity p " +
		       "WHERE p.clientCode = :clientCode " +
		       "AND (SUBSTRING(p.txnDate, 1, 10) = :dateStr OR SUBSTRING(p.razorpayCreatedAtEpoch, 1, 10) = :dateStr) " +
		       " AND (UPPER(p.status) = 'COMPLETED' OR UPPER(p.status) = 'SUCCESS')\r\n"
		       )
		Double sumAmountByClientAndDate(@Param("clientCode") String clientCode,
		                                @Param("dateStr") String dateStr);

	

}
