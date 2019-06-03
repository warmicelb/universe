package com.ice.universe.domain;

import com.ice.universe.extend.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;

@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private Integer status;

}