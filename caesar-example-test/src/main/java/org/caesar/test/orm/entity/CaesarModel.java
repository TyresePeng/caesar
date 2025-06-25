package org.caesar.test.orm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

/**
 * @author GuoPeng
 */
@Data
@TableName("crm_doudian_order")
public class CaesarModel {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(jdbcType = JdbcType.BIGINT)
    private String orderId;

}