package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.model.entity.XLRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLRouteRepository extends JpaRepository<XLRoute, Long> {
//    List<XLRoute> findByParentId(Long parentId);
}
