package in.codifi.ambalal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.ambalal.entity.AdminLoginEntity;

public interface AdminLoginRepository extends JpaRepository<AdminLoginEntity,Long> {

	AdminLoginEntity findByEmailId(String emailId);
	
	AdminLoginEntity findByEmailIdAndPassword(String emailId,String password);
}