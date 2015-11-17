package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by DrScott on 11/13/15.
 */
@Entity
public class Game{
    @Id
    @GeneratedValue
    @Column(nullable = false)
   public int id;

    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public String system;



    @ManyToOne
    public User user;
}
