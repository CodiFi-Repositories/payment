package in.codifi.ambalal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.codifi.ambalal.entity.CredentialKey;

public interface CredentialKeyRepositiory extends CrudRepository<CredentialKey, Long> {

	List<CredentialKey> findByType(String type);

	List<CredentialKey> findAllByType(String string);

}
