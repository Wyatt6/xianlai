package fun.xianlai.mod.toolkit.repository;

import fun.xianlai.mod.toolkit.model.entity.SecretCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface SecretCodeRepository extends JpaRepository<SecretCode, Long> {

    @Query("select distinct new SecretCode(s.id, s.tenant, s.sortId, s.category, s.title, s.username, s.code, s.tips, " +
            "                              s.twoFAS, s.appleId, s.wechat, s.alipay, s.phone, s.email, s.remark) " +
            " from SecretCode s " +
            " where s.tenant = ?1 " +
            "      and (?2 is null or s.title like %?2%) " +
            "      and (?2 is null or s.title like %?2%)")
    Page<SecretCode> findConditionally(@NonNull Long tenant, String category, String title, Pageable pageable);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, s.id as id " +
            "      from tb_toolkit_secret_code s, (select @rownum \\:= 0) n " +
            "      order by s.category asc, s.sort_id asc, s.title asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

}
