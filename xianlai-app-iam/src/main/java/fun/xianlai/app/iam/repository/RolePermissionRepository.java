package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.RolePermission;
import fun.xianlai.app.iam.model.entity.rbac.pk.RolePermissionPK;
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
