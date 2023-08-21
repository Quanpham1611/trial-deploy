package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.MilkTea;

@Repository
public interface MilkTeaRepository extends JpaRepository<MilkTea, String> {
    @Query(value = "SELECT * FROM milk_tea mt WHERE mt.name LIKE %?1% and mt.enabled = true", nativeQuery = true)
    List<MilkTea> findMilkTeaByName(String name);

    @Query(value = "SELECT * FROM milk_tea mt WHERE mt.milk_tea_id = ?1 and mt.enabled = true" , nativeQuery = true)
    Optional<MilkTea> findById(String id);

    @Query(value = "SELECT * FROM milk_tea mt WHERE mt.enabled = true" , nativeQuery = true)
    List<MilkTea> findAll();
}