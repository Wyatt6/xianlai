package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.entity.XLPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLPathRepository extends JpaRepository<XLPath, Long> {
}
