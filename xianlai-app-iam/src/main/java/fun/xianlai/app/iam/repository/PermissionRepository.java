package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("select distinct new Permission(p.id, p.identifier, p.name, p.description, p.sortId) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.active = true " +
            "      inner join RolePermission rp on ur.roleId = rp.roleId " +
            "      inner join Permission p on rp.permissionId = p.id " +
            " where ur.userId = ?1")
    List<Permission> findActivePermissionsByUserId(Long userId);
}
