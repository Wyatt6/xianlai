package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.other.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
