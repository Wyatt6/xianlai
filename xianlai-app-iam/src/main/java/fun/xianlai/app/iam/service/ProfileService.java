package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.other.Profile;

/**
 * @author WyattLau
 */
public interface ProfileService {
    /**
     * 为新用户创建用户详情
     *
     * @param userId 用户ID
     */
    void createProfile(Long userId);

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    Profile getProfile(Long userId);
}
