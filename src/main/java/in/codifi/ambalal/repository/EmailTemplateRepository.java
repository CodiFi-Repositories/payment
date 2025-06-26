package in.codifi.ambalal.repository;

import org.springframework.data.repository.CrudRepository;

import in.codifi.ambalal.entity.EmailTemplateEntity;

public interface EmailTemplateRepository extends CrudRepository<EmailTemplateEntity, Long> {

	EmailTemplateEntity findByKeyData(String keyData);
}
