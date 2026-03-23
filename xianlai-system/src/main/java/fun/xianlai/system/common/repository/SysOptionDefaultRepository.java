package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.SysOptionDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface SysOptionDefaultRepository extends JpaRepository<SysOptionDefault, Long> {
    List<SysOptionDefault> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<SysOptionDefault> findByScopeAndScopeIdAndFrontLoad(String scope, Long scopeId, Boolean frontLoad);

    List<SysOptionDefault> findByScope(String scope);

    List<SysOptionDefault> findByScopeAndScopeId(String scope, Long ScopeId);
//
//    Optional<SysOptionDefault> findByOptionKey(String optionKey);
}
