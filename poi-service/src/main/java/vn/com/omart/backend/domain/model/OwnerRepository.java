package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findByCreatedBy(String username);

    Owner findByUserId(String userId);
    
    Owner findByPhoneNumber(String phoneNumber);

}
