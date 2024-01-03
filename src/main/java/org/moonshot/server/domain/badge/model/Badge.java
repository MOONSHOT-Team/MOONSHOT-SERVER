package org.moonshot.server.domain.badge.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.userBadge.model.UserBadge;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "badge")
    private List<UserBadge> userList = new ArrayList<>();

}
