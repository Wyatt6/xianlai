package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.XLOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface XLOptionRepository extends JpaRepository<XLOption, Long> {
    List<XLOption> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<XLOption> findByScopeAndScopeIdAndFrontLoad(String scope, Long scopeId, Boolean frontLoad);

    List<XLOption> findByScope(String scope);

    List<XLOption> findByScopeAndScopeId(String scope, Long scopeId);

    Optional<XLOption> findByOptionKey(String optionKey);
}
