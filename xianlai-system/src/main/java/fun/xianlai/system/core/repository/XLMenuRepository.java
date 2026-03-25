package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.model.entity.XLMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface XLMenuRepository extends JpaRepository<XLMenu, Long> {
    List<XLMenu> findByActive(Boolean active, Sort sort);
//
//    List<XLMenu> findByParentId(Long parentId);
}
