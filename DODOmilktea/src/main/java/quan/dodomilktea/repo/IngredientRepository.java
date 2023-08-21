package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String>  {
    @Query(value = "SELECT * FROM ingredient i WHERE i.ingredient_id = ?1 and i.enabled = true" , nativeQuery = true)
    Optional<Ingredient> findById(String id);

    @Query(value = "SELECT * FROM ingredient i WHERE i.enabled = true" , nativeQuery = true)
    List<Ingredient> findAll();

    @Query(value = "SELECT * FROM ingredient i WHERE i.manager_id = ?1 and i.enabled = true" , nativeQuery = true)
    List<Ingredient> findAllManage(String accId);
}
