package com.hjg.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;
    @CreatedDate
    @Column(updatable = false, nullable = false, columnDefinition = "datetime")
    private Date sendTime;
    @Column(length = 20)
    private String username;
    @Column(length = 20)
    private String nickname;
    @Column(columnDefinition = "text")
    private String content;

}
