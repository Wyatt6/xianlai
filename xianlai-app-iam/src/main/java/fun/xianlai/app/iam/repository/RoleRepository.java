package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.Role;
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
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select distinct new Role(r.id, r.sortId, r.identifier, r.name, r.description, r.active, r.bindCheck) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.active = true " +
            " where ur.userId = ?1")
    List<Role> findActiveRolesByUserId(Long userId);

    @Query("select distinct new Role(r.id, r.sortId, r.identifier, r.name, r.description, r.active, r.bindCheck) " +
            " from Role r " +
            "      left join RolePermission rp on r.id = rp.roleId " +
            "      left join Permission p on rp.permissionId = p.id " +
            " where (?1 is null or r.identifier like %?1%) " +
            "      and (?2 is null or r.name like %?2%) " +
            "      and (?3 is null or r.description like %?3%) " +
            "      and (?4 is null or r.active = ?4) " +
            "      and (?5 is null or r.bindCheck = ?5) " +
            "      and (?6 is null or p.identifier like %?6%)")
    Page<Role> findConditionally(String identifier,
                                 String name,
                                 String description,
                                 Boolean active,
                                 Boolean bindCheck,
                                 String permission,
                                 Pageable pageable);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, r.id as id, r.sort_id " +
            "      from tb_iam_role r, (select @rownum \\:= 0) n " +
            "      order by r.sort_id asc, r.identifier asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

    @Query("select ur.roleId from UserRole ur where ur.userId = ?1")
    List<Long> findIdsByUserId(Long userId);

    List<Role> findByBindCheck(Boolean bindCheck);
}
