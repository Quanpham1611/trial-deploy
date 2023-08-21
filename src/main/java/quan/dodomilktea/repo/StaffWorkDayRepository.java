package quan.dodomilktea.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.StaffWorkDay;
import quan.dodomilktea.model.StaffWorkDayKey;

@Repository
public interface StaffWorkDayRepository extends JpaRepository<StaffWorkDay, StaffWorkDayKey> {
    @Query(value = "select * from staff_work_day swd where swd.work_day=?1", nativeQuery = true)
    List<StaffWorkDay> findByWorkDay(LocalDate date);

    @Query(value = "select * from staff_work_day s where s.staff_id=?1", nativeQuery = true)
    List<StaffWorkDay> findByStaffId(String accountId);
}
