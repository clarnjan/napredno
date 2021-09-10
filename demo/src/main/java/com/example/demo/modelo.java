package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class modelo {
    @Id
    private int id;
    private String name;
}
