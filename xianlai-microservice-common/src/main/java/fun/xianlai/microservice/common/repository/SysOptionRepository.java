package fun.xianlai.microservice.common.repository;

import fun.xianlai.microservice.common.model.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface SysOptionRepository extends JpaRepository<SysOption, String> {
    List<SysOption> findByFrontLoad(Boolean frontLoad);

    List<SysOption> findByBackLoad(Boolean backLoad);

    Optional<SysOption> findByKeyAndBackLoad(String key, Boolean backLoad);
}
