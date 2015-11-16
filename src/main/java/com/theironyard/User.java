package com.theironyard;


import org.springframework.jmx.export.annotation.ManagedAttribute;

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
    Integer id;

    String name;
    String password;


    @OneToMany(mappedBy = "user")
    List<Game> userGames;

}
