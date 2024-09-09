package com.thesis.BackendApp.model;

import lombok.*;



@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Cryptocurrency {

    private String id;

    private String rank;

    private String symbol;

    private String name;

    private String supply;

    private String marketCap;

    private String price;

    private String volume24h;

    private String change24h;

    private String vwap24h;


}
