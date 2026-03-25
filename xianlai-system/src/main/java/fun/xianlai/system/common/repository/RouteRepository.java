package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByParentId(Long parentId);
}
