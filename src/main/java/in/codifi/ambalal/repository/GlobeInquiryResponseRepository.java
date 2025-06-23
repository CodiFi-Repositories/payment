package in.codifi.ambalal.repository;

import org.springframework.data.repository.CrudRepository;

import in.codifi.ambalal.entity.GlobeInquiryResponse;


public interface GlobeInquiryResponseRepository extends CrudRepository<GlobeInquiryResponse, Long> {

	 GlobeInquiryResponse findByMsgId(String msgId); // Optional, if needed
}
