package probeV.GameInfogg.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.BaseTimeEntity;
import probeV.GameInfogg.domain.user.constant.RoleType;

import java.util.ArrayList;
import java.util.List;



@Getter @Setter
@NoArgsConstructor
@Table(name = "USERS")
@Entity
public class User extends BaseTimeEntity {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* Relation */
    @OneToMany(mappedBy = "user")
    private List<UserTask> userTasks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserItem> userItems = new ArrayList<>();

    /* Attribute */
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "attribute_code", nullable = false)
    private String attributeCode;

    @Builder
    public User(String email, String name, RoleType roleType, String provider, String attributeCode) {
        this.email = email;
        this.name = name;
        this.roleType = roleType;
        this.provider = provider;
        this.attributeCode = attributeCode;
    }

    public User update(String name, String email){
        this.name = name;
        this.email = email;

        return this;
    }

    public String getRoleTypeKey(){
        return this.roleType.getKey();
    }
}
