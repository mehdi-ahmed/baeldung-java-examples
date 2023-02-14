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
public class User implements Serializable, Cloneable {

    private String firstName;
    private String lastName;
    private Address address;

    public User(User that) {
        this(that.getFirstName(), that.getLastName(), new Address(that.getAddress()));
    }

    public User(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    @Override
    public Object clone() {
        User user;
        try {
            user = (User) super.clone();

        } catch (CloneNotSupportedException e) {
            user = new User(this.getFirstName(), this.getLastName(), this.getAddress());
        }

        // super.clone() call returns a shallow copy of an object,
        // but we set deep copies of mutable fields manually, so the result is correct.
        user.address = (Address) this.address.clone();
        return user;
    }
}
