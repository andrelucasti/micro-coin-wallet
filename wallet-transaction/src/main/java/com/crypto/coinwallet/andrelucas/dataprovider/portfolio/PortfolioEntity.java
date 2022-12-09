package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "PORTFOLIO")
public class PortfolioEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;


    public PortfolioEntity(){}

    public PortfolioEntity(UUID id, String name){
        this.id = id;
        this.name = name;
    }
}
