package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    @Query(value = "SELECT * FROM report r WHERE r.report_id = ?1 and r.enabled = true" , nativeQuery = true)
    Optional<Report> findById(String id);

    @Query(value = "SELECT * FROM report r WHERE r.enabled = true" , nativeQuery = true)
    List<Report> findAll();
}