package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.XLOptionDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface XLOptionDefaultRepository extends JpaRepository<XLOptionDefault, Long> {
    List<XLOptionDefault> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<XLOptionDefault> findByScope(String scope);

    Optional<XLOptionDefault> findByOptionKey(String optionKey);
}
