package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author WyattLau
 */
@Repository
public interface SysApiRepository extends JpaRepository<SysApi, Long> {
    @Query("select distinct new SysApi(a.id, a.callPath, a.description, a.requestMethod, a.url) " +
            " from SysApi a " +
            " where (?1 is null or a.callPath like %?1%) " +
            "      and (?2 is null or a.description like %?2%) " +
            "      and (?3 is null or a.requestMethod = ?3) " +
            "      and (?4 is null or a.url like %?4%)")
    Page<SysApi> findConditionally(String callPath,
                                   String description,
                                   RequestMethod requestMethod,
                                   String url,
                                   Pageable pageable);
}
