package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("select distinct new Permission(p.id, p.sortId, p.identifier, p.name, p.description) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.active = true " +
            "      inner join RolePermission rp on ur.roleId = rp.roleId " +
            "      inner join Permission p on rp.permissionId = p.id " +
            " where ur.userId = ?1")
    List<Permission> findActivePermissionsByUserId(Long userId);

    @Query("select distinct new Permission(p.id, p.sortId, p.identifier, p.name, p.description) " +
            " from Permission p " +
            " where (?1 is null or p.identifier like %?1%) " +
            "      and (?2 is null or p.name like %?2%) " +
            "      and (?3 is null or p.description like %?3%)")
    Page<Permission> findConditionally(String identifier, String name, String description, Pageable pageable);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, p.id as id, p.identifier, p.sort_id " +
            "      from tb_iam_permission p, (select @rownum \\:= 0) n " +
            "      order by p.sort_id asc, p.identifier asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

    @Query("select rp.permissionId from RolePermission rp where rp.roleId = ?1")
    List<Long> findIdsByRoleId(Long roleId);

}
