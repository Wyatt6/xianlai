package fun.xianlai.system.core.convert;

import fun.xianlai.system.core.entity.XLTenant;
import fun.xianlai.system.dto.XLTenantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author WyattLau
 */
@Mapper
public interface XLTenantConverter {
    XLTenantConverter INSTANCE = Mappers.getMapper(XLTenantConverter.class);    // 单例

    XLTenantDTO toDto(XLTenant entity);

    XLTenant toEntity(XLTenantDTO dto);
}
