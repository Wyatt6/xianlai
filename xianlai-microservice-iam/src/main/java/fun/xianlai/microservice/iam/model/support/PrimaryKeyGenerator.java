package fun.xianlai.microservice.iam.model.support;

import fun.xianlai.basic.utils.FakeSnowflakeUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 主键ID生成器
 * 使用FakeSnowflakeUtil中定义的伪雪花算法
 *
 * @author WyattLau
 */
public class PrimaryKeyGenerator implements IdentifierGenerator {
    synchronized public long next() {
        return FakeSnowflakeUtil.nextId();
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        return IdentifierGenerator.super.generate(session, owner, currentValue, eventType);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return next();
    }
}
