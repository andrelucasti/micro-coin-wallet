package com.crypto.walletmanager.dataprovider.portfolio;

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
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "USER_ID")
    private UUID userId;

    public PortfolioEntity() {
    }

    public PortfolioEntity(String name, UUID userId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.userId = userId;
    }

}