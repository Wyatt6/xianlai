package fun.xianlai.app.common.model.entity;

import fun.xianlai.core.supprt.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 系统接口
 *
 * @author WyattLau
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_sys_api", indexes = {
        @Index(columnList = "callPath", unique = true)
})
public class SysApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(1000) not null")
    private String callPath;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null")
    private RequestMethod requestMethod;

    @Column(columnDefinition = "varchar(1000) not null")
    private String url;

    public SysApi() {}

    public SysApi(Long id, String callPath, RequestMethod requestMethod, String url) {
        this.id = id;
        this.callPath = callPath;
        this.requestMethod = requestMethod;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCallPath() {
        return callPath;
    }

    public void setCallPath(String callPath) {
        this.callPath = callPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
