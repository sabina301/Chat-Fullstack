package com.example.websocketchat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    @Column(name="role_id")
    private Integer roleId;

    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }
    public Role(String authority){
        this.authority = authority;
    }

}