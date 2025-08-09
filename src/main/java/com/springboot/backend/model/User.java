package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}
        )
})
@SecondaryTables({
        @SecondaryTable(name = "tbl_accounts", pkJoinColumns = {
                @PrimaryKeyJoinColumn(name="user_id",  referencedColumnName = "user_id")}),
})
public class User{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    long id;


    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;


    @Column(name = "email", nullable = false)
    @Email
    private String userEmail;

    @Column(name = "pic_url")
    private String pic_url;

    //@ApiModelProperty(notes="Password should atleast be 8 characters long")

    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany()
    @JoinColumn(name = "field_id")
    private List<Field> fields = new ArrayList<>();

    @Column(table = "tbl_accounts", name = "role", length = 20)
    //@ColumnDefault("role_admin")
    private String role = "Admin";

    @Column(table = "tbl_accounts", name = "is_locked")
    private Boolean isLocked;

    @Column(table = "tbl_accounts", name = "is_enabled")
    private Boolean isEnabled;

    private int delete_status = 0;


}
