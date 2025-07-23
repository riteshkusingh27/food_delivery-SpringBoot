package com.food.delivery.io;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
public class AuthenticationResponse {

    private String email ;
    private String token ;
}
