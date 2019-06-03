package com.ice.universe.extend.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @ClassName BaseEntiy
 * @Description TODO
 * @Author liubin
 * @Date 2019/5/31 11:42 AM
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity{

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private DateTime gmtCreated;

    private DateTime gmtModified;
}
