package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.OptionDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface OptionDefaultRepository extends JpaRepository<OptionDefault, Long> {
    List<OptionDefault> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<OptionDefault> findByScope(String scope);

    Optional<OptionDefault> findByOptionKey(String optionKey);
}
