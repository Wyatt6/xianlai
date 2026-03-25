package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
    @Query("select distinct new Path(p.id, p.sortId, p.name, p.path) " +
            " from Path p " +
            " where (?1 is null or p.name like %?1%) " +
            "      and (?2 is null or p.path like %?2%)")
    Page<Path> findConditionally(String name,
                                 String path,
                                 Pageable pageable);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, p.id as id, p.sort_id, p.name " +
            "      from tb_common_path p, (select @rownum \\:= 0) n " +
            "      order by p.sort_id asc, p.name asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);
}
