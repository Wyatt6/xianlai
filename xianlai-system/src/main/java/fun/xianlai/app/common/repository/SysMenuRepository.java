package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {

    List<SysMenu> findByActive(Boolean active, Sort sort);

    List<SysMenu> findByParentId(Long parentId);

}
