package in.codifi.ambalal.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.codifi.ambalal.entity.EmailLogEntity;

public interface EmailLogRepository extends CrudRepository<EmailLogEntity, Long> {

	List<EmailLogEntity> findByIdBetween(long startId, long endId);

	List<EmailLogEntity> findByCreatedOnBetweenAndReqLogSubNotIn(Date fromDate, Date toDate, String subject);

	List<EmailLogEntity> findByEmailIdAndCreatedOnBetweenAndReqLogSubNotIn(String emailId, Date fromDate, Date toDate,
			String subject);
}
