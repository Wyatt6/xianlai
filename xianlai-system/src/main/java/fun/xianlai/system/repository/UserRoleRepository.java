package fun.xianlai.system.repository;

import fun.xianlai.system.model.entity.rbac.UserRole;
import fun.xianlai.system.model.entity.rbac.pk.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    void deleteByRoleId(Long roleId);
}
