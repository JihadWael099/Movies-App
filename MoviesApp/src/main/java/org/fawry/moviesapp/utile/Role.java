package org.fawry.moviesapp.utile;

public enum Role {
    ROLE_ADMIN,
    ROLE_USER;


    public String withoutPrefix() {
        return this.name().replace("ROLE_", "");
    }
}
