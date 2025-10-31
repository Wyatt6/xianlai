package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface SysPathRepository extends JpaRepository<SysPath, Long> {
    @Query("select distinct new SysPath(p.id, p.sortId, p.name, p.path) " +
            " from SysPath p " +
            " where (?1 is null or p.name like %?1%) " +
            "      and (?2 is null or p.path like %?2%)")
    Page<SysPath> findConditionally(String name,
                                    String path,
                                    Pageable pageable);
}
