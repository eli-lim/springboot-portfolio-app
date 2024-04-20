package com.example.portfolio.security;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("OPTION")
public class Option extends Security {

    @Column
    private Date expiration;

    @Column
    private Double strike;

    @Column
    @Enumerated(EnumType.STRING)
    private OptionType optionType;

    /**
     * The underlying security that the option is based on.
     */
    @ManyToOne
    private Stock underlying;

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Double getStrike() {
        return strike;
    }

    public void setStrike(Double strike) {
        this.strike = strike;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public Stock getUnderlying() {
        return underlying;
    }

    public void setUnderlying(Stock underlying) {
        this.underlying = underlying;
    }
}
