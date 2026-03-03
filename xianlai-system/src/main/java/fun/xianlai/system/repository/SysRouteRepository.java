package fun.xianlai.system.repository;

import fun.xianlai.system.model.entity.SysRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface SysRouteRepository extends JpaRepository<SysRoute, Long> {
    List<SysRoute> findByParentId(Long parentId);
}
