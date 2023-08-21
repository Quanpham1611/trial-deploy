package quan.dodomilktea.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.WorkDay;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, LocalDate>{
    @Query(value = "SELECT * FROM work_day w WHERE w.work_day = ?1 and w.enabled = true" , nativeQuery = true)
    WorkDay findByDate(LocalDate date);

    @Query(value = "SELECT * FROM work_day w WHERE w.enabled = true" , nativeQuery = true)
    List<WorkDay> findAll();
}