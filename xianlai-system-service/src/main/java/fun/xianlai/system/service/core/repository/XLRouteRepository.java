package fun.xianlai.system.service.core.repository;

import fun.xianlai.system.service.core.model.entity.XLRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLRouteRepository extends JpaRepository<XLRoute, Long> {
//    List<XLRoute> findByParentId(Long parentId);
}
