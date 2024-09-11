package com.assgn.yourssu.domain;
import com.assgn.yourssu.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 액세스레벨 왜 이렇게 함?
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // 얘도
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String username;
}
