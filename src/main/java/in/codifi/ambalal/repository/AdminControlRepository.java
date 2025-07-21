package in.codifi.ambalal.repository;

import in.codifi.ambalal.entity.KraKeyValueEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdminControlRepository extends CrudRepository<KraKeyValueEntity, Long> {
	

	Optional<KraKeyValueEntity> findByKraKey(String kraKey);
	
}