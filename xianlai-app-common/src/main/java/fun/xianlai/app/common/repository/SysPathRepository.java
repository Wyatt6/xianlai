package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface SysPathRepository extends JpaRepository<SysPath, String> {
}
