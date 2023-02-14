package com.mytutorials.baeldung.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Address implements Serializable, Cloneable {
    private String street;
    private String city;
    private String country;
    public Address(Address that) {
        this(that.getStreet(), that.getCity(), that.getCountry());
    }
    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new Address(this.street, this.city, this.country);
        }
    }
}
