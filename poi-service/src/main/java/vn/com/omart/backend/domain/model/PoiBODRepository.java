package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiBODRepository extends JpaRepository<PoiBOD, Long> {

	public List<PoiBOD> findByUserBOD(UserBOD userBOD);

	public PoiBOD findByPoi(PointOfInterest poi);

	public List<PoiBOD> findByUserBOD(UserBOD userBOD, Pageable pageable);

}
