package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface SysOptionRepository extends JpaRepository<SysOption, Long> {
    List<SysOption> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<SysOption> findByScopeAndScopeIdAndFrontLoad(String scope, Long scopeId, Boolean frontLoad);

    List<SysOption> findByScope(String scope);

    List<SysOption> findByScopeAndScopeId(String scope, Long scopeId);
//
//    Optional<SysOptionDefault> findByOptionKey(String optionKey);
}
