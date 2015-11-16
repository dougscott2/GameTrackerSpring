package com.theironyard.entities;


import com.theironyard.entities.Game;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DrScott on 11/13/15.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(nullable =false)
    int id;

    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String password;


    @OneToMany(mappedBy = "user")
    public List<Game> userGames;

}
