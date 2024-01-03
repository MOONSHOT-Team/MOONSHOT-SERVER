package org.moonshot.server.domain.Log.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogState state;

    private int prevNum;

    @Column(nullable = false)
    private int currNum;

    @Column(nullable = false)
    private String content;

}
