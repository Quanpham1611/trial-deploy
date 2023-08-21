package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String>  {
    @Query(value = "SELECT * FROM staff s WHERE s.id_number = ?1 and s.enabled = true" , nativeQuery = true)
    Optional<Staff> findById(String id);

    @Query(value = "SELECT * FROM staff s WHERE s.enabled = true" , nativeQuery = true)
    List<Staff> findAll();

    @Query(value = "select * from staff s where s.deliver_acc_id=?1 and enabled=true", nativeQuery = true)
    Staff findShipperByAccountId(String accountId);

    @Query(value = "select * from staff s where s.staff_role = ?1 and enabled = true", nativeQuery = true)
    List<Staff> findByRole(String role);
}
