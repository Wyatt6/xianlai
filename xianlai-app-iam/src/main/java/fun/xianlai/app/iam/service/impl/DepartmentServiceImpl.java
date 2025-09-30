package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.other.Department;
import fun.xianlai.app.iam.repository.DepartmentRepository;
import fun.xianlai.app.iam.service.DepartmentService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @SimpleServiceLog("获取部门")
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }
}
