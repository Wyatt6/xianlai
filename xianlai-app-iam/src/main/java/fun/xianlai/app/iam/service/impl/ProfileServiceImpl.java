package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.repository.ProfileRepository;
import fun.xianlai.app.iam.service.ProfileService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    @SimpleServiceLog("为新用户创建用户详情")
    public void createProfile(Long userId) {
        Profile newProfile = new Profile();
        newProfile.setUserId(userId);
        profileRepository.save(newProfile);
    }

    @Override
    @SimpleServiceLog("获取用户详情")
    public Profile getProfile(Long userId) {
        return profileRepository.findById(userId).orElse(null);
    }
}
