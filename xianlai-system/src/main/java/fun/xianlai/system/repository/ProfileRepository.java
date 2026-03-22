package fun.xianlai.system.repository;

import fun.xianlai.system.model.entity.other.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
