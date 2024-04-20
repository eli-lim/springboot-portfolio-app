package com.example.portfolio.position;

import com.example.portfolio.security.Security;

public class Position {

    private Security security;

    private long size;

    public Position(Security security, long size) {
        this.security = security;
        this.size = size;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
