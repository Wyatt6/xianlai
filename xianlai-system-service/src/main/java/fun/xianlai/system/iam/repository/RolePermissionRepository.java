package fun.xianlai.system.iam.repository;

import fun.xianlai.system.iam.model.entity.RolePermission;
import fun.xianlai.system.iam.model.entity.pk.RolePermissionPK;
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
