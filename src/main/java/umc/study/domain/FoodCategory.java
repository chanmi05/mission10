package umc.study.domain;

import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id; // MongoDB에서 사용하는 것임!
import umc.study.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;


}
