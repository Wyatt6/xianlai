package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.rbac.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author WyattLau
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("select distinct new User(u.id, u.username, u.password, u.salt, u.registerTime, u.active) " +
            " from User u " +
            "      left join UserRole ur on u.id = ur.userId " +
            "      left join Role r on ur.roleId = r.id " +
            "      left join RolePermission rp on r.id = rp.roleId " +
            "      left join Permission p on rp.permissionId = p.id " +
            " where (?1 is null or u.username like %?1%) " +
            "      and (?2 is null or u.registerTime >= ?2) " +
            "      and (?3 is null or u.registerTime <= ?3) " +
            "      and (?4 is null or u.active = ?4) " +
            "      and (?5 is null or r.identifier like %?5%) " +
            "      and (?6 is null or p.identifier like %?6%)")
    Page<User> findConditionally(String username,
                                 Date stRegistryTime,
                                 Date edRegistryTime,
                                 Boolean active,
                                 String role,
                                 String permission,
                                 Pageable pageable);
}
