package in.codifi.ambalal.repository;

import in.codifi.ambalal.entity.KraKeyValueEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KraKeyValueRepository extends CrudRepository<KraKeyValueEntity, Long> {
    List<KraKeyValueEntity> findByMasterIdAndMasterName(String masterId, String masterName);
}
