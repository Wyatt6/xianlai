package fun.xianlai.system.repository;

import fun.xianlai.system.model.entity.rbac.RolePermission;
import fun.xianlai.system.model.entity.rbac.pk.RolePermissionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPK> {

    void deleteByPermissionId(Long permissionId);

    void deleteByRoleId(Long roleId);

}
