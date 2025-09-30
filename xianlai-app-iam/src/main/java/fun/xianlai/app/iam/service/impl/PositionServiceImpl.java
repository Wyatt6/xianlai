package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.other.Position;
import fun.xianlai.app.iam.repository.PositionRepository;
import fun.xianlai.app.iam.service.PositionService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;

    @Override
    @SimpleServiceLog("获取职位")
    public Position getPosition(Long id) {
        return positionRepository.findById(id).orElse(null);
    }
}
