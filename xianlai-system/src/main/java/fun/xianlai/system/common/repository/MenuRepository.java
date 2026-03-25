package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByActive(Boolean active, Sort sort);

    List<Menu> findByParentId(Long parentId);
}
