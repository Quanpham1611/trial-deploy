package quan.dodomilktea.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, String> {

    @Query(value = "select * from salary s where s.role = ?1 and s.on_time=?2 and s.enabled=true", nativeQuery = true)
    Salary findSalaryByRole(String role, boolean isOnTime);
}
