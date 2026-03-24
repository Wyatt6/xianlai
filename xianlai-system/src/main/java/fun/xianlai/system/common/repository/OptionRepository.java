package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<Option> findByScopeAndScopeIdAndFrontLoad(String scope, Long scopeId, Boolean frontLoad);

    List<Option> findByScope(String scope);

    List<Option> findByScopeAndScopeId(String scope, Long scopeId);

    Optional<Option> findByOptionKey(String optionKey);
}
