package com.hjg.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "fieldHandler"})
public class User implements Serializable {

    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    @Column(length = 20, unique = true, nullable = false)
    private String username;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 20)
    private String nickname;
    @Column(insertable = false, nullable = false, columnDefinition = "int(1) default 0")
    private Integer status;
    @Column(columnDefinition = "datetime")
    private Date onlineTime;
    @LastModifiedDate
    @Column(columnDefinition = "datetime")
    private Date updateTime;
    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private Date createTime;


}
