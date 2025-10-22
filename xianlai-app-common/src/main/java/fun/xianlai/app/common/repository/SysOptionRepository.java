package fun.xianlai.app.common.repository;

import fun.xianlai.app.common.model.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface SysOptionRepository extends JpaRepository<SysOption, Long> {
    List<SysOption> findByFrontLoad(Boolean frontLoad);

    List<SysOption> findByBackLoad(Boolean backLoad);

    Optional<SysOption> findByOptionKeyAndBackLoad(String optionKey, Boolean backLoad);
}
