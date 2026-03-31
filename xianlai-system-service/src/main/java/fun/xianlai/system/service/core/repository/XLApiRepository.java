package fun.xianlai.system.service.core.repository;

import fun.xianlai.system.service.core.model.entity.XLApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLApiRepository extends JpaRepository<XLApi, Long> {
//    @Query("select distinct new SysApi(a.id, a.callPath, a.description, a.requestMethod, a.url) " +
//            " from SysApi a " +
//            " where (?1 is null or a.callPath like %?1%) " +
//            "      and (?2 is null or a.description like %?2%) " +
//            "      and (?3 is null or a.requestMethod = ?3) " +
//            "      and (?4 is null or a.url like %?4%)")
//    Page<XLApi> findConditionally(String callPath,
//                                  String description,
//                                  RequestMethod requestMethod,
//                                  String url,
//                                  Pageable pageable);
//
//    @Query(value = "select num " +
//            " from (select @rownum \\:= @rownum + 1 as num, a.id as id, a.call_path " +
//            "      from tb_common_sys_api a, (select @rownum \\:= 0) n " +
//            "      order by a.call_path asc) t " +
//            " where t.id = ?1", nativeQuery = true)
//    Long findRowNumById(Long id);
}
