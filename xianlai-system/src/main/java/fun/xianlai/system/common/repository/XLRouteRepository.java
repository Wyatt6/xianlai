package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.XLRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLRouteRepository extends JpaRepository<XLRoute, Long> {
//    List<XLRoute> findByParentId(Long parentId);
}
