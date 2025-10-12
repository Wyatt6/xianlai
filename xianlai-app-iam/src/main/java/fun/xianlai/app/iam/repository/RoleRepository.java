package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select distinct new Role(r.id, r.identifier, r.name, r.description, r.active, r.sortId) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.active = true " +
            " where ur.userId = ?1")
    List<Role> findActiveRolesByUserId(Long userId);
}
