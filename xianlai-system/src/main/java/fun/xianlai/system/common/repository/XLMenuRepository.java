package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.XLMenu;
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
