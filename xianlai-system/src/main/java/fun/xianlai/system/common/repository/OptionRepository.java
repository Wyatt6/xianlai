package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface OptionRepository extends JpaRepository<SysOption, Long> {
    List<SysOption> findByTypeAndFrontLoad(String type, Boolean frontLoad);

    List<SysOption> findByTypeAndIdentifierAndFrontLoad(String type, Long identifier, Boolean frontLoad);

    List<SysOption> findByType(String type);

    List<SysOption> findByTypeAndIdentifier(String type, Long identifier);

    Optional<SysOption> findByOptionKey(String optionKey);
}
