package in.codifi.ambalal.repository;

import in.codifi.ambalal.entity.TblAdminControlEntity;
import org.springframework.data.repository.CrudRepository;

public interface TblAdminControlRepository extends CrudRepository<TblAdminControlEntity, Long> {
	
	TblAdminControlEntity findByUserId(String userId);

}

