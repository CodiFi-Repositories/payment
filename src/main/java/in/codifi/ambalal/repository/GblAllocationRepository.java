package in.codifi.ambalal.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.codifi.ambalal.entity.GlobePaidAmtEntity;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface GblAllocationRepository extends CrudRepository<GlobePaidAmtEntity, Long> {

//    @Query("SELECT g.amount FROM GlobePaidAmtEntity g WHERE g.clicode = :clientCode")
//    Double findByClientCode(String clientCode);
    
    @Query("select coalesce(sum(g.amount), 0) from GlobePaidAmtEntity g where g.clicode = :clientCode")
    Double findByClientCode(@Param("clientCode") String clientCode);

   
    
}