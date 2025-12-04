package fun.xianlai.app.iam.repository;

import fun.xianlai.app.iam.model.entity.other.UserInfo;
import fun.xianlai.app.iam.model.entity.rbac.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // UserInfo没用@Entity注解，所以这里要写全路径
    @Query("select distinct new fun.xianlai.app.iam.model.entity.other.UserInfo(" +
            "      u.id, u.username, u.registerTime, u.active, u.isDelete, " +
            "      pf.avatar, pf.nickname, pf.gender, pf.phone, pf.email) " +
            " from User u " +
            "      inner join Profile pf on u.id = pf.userId " +
            "      left join UserRole ur on u.id = ur.userId " +
            "      left join Role r on ur.roleId = r.id " +
            "      left join RolePermission rp on r.id = rp.roleId " +
            "      left join Permission p on rp.permissionId = p.id " +
            " where (?1 is null or u.username like %?1%) " +
            "      and (?2 is null or u.registerTime >= ?2) " +
            "      and (?3 is null or u.registerTime <= ?3) " +
            "      and (?4 is null or u.active = ?4) " +
            "      and (?5 is null or u.isDelete = ?5) " +
            "      and (?6 is null or pf.nickname like %?6%) " +
            "      and (?7 is null or pf.gender = ?7) " +
            "      and (?8 is null or pf.phone like %?8%) " +
            "      and (?9 is null or pf.email like %?9%) " +
            "      and (?10 is null or r.identifier like %?10%) " +
            "      and (?11 is null or p.identifier like %?11%)")
    Page<UserInfo> findConditionally(String username,
                                     Date stRegisterTime,
                                     Date edRegisterTime,
                                     Boolean active,
                                     Boolean isDelete,
                                     String nickname,
                                     String gender,
                                     String phone,
                                     String email,
                                     String role,
                                     String permission,
                                     Pageable pageable);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, u.id as id " +
            "      from tb_iam_user u, (select @rownum \\:= 0) n " +
            "      order by u.id) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

}
