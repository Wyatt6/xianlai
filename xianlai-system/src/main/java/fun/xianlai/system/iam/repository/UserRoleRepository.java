package fun.xianlai.system.iam.repository;

import fun.xianlai.system.iam.model.entity.UserRole;
import fun.xianlai.system.iam.model.entity.pk.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    void deleteByRoleId(Long roleId);
}
