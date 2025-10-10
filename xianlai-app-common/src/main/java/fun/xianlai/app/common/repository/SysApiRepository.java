package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface SysApiRepository extends JpaRepository<SysApi, String> {
}
