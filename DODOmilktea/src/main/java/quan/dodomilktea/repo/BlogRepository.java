package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    @Query(value = "SELECT * FROM blog b WHERE b.blog_id=?1 and b.enabled = true" , nativeQuery = true)
    Optional<Blog> findById(String id);

    @Query(value = "SELECT * FROM blog b WHERE b.enabled = true" , nativeQuery = true)
    List<Blog> findAll();
}
