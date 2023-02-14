package com.mytutorials.baeldung;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mytutorials.baeldung.model.Address;
import com.mytutorials.baeldung.model.User;
import org.apache.commons.lang.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * How to Make a Deep Copy of an Object in Java
 * <a href="https://www.baeldung.com/java-deep-copy">...</a>
 */

public class DeepAndShallowCopiesTest {

    // Shallow copies
    @Test
    public void whenShallowCopying_thenObjectShouldNotBeSame() {

        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);

        User shallowCopyUser = new User(
                pm.getFirstName(), pm.getLastName(), pm.getAddress());

        System.out.println((shallowCopyUser == pm)); // false

        // returns true but this gives false if we don't implement equals() and hashcode
        System.out.println((shallowCopyUser.equals(pm)));


        // If we want to ensure that two objects don’t refer to the same object,
        // we have to write our assertion by invoking the isNotSameAs()
        assertThat(shallowCopyUser).isNotSameAs(pm);
    }
    @Test
    public void whenModifyingOriginalObject_ThenCopyShouldChange() {

        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        User shallowCopyUser = new User(
                pm.getFirstName(), pm.getLastName(), pm.getAddress());

        address.setCountry("Great Britain");

        assertThat(shallowCopyUser).isNotSameAs(pm);

        // true: Both objects, the original and shallow copy were modified
        System.out.println((shallowCopyUser.equals(pm)));
    }


    // Deep Copies
    @Test
    public void whenModifyingOriginalObject_thenCopyShouldNotChange() {
        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        User deepCopy = new User(pm);

        assertThat(deepCopy).isNotSameAs(pm);
        address.setCountry("Great Britain");

        assertNotEquals(
                pm.getAddress().getCountry(),
                deepCopy.getAddress().getCountry());

        // pm object's country = Great Britain
        // deepCopy object's country = England
        // ==> The copy didn't It didn't change
    }
    @Test
    public void whenModifyingOriginalObject_thenCloneCopyShouldNotChange() {
        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        User deepCopy = (User) pm.clone();

        // Before modification
        assertThat(deepCopy.getAddress().getCountry())
                .isEqualTo(pm.getAddress().getCountry());
        System.out.println(deepCopy.equals(pm)); // true

        address.setCountry("Great Britain");

        // After modification
        // deepCopy kept its original value "England"
        assertThat(deepCopy.getAddress().getCountry())
                .isNotEqualTo(pm.getAddress().getCountry());
        System.out.println(deepCopy.equals(pm)); // false
    }

    // External Libs

    // Apache Commons Lang
    @Test
    public void whenModifyingOriginalObject_thenCommonsCloneShouldNotChange() {
        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        User deepCopy = (User) SerializationUtils.clone(pm);

        address.setCountry("Great Britain");

        assertThat(deepCopy.getAddress().getCountry())
                .isNotEqualTo(pm.getAddress().getCountry());
    }

    // Gson
    @Test
    public void whenModifyingOriginalObject_thenGsonCloneShouldNotChange() {
        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        Gson gson = new Gson();
        User deepCopy = gson.fromJson(gson.toJson(pm), User.class);

        address.setCountry("Great Britain");

        assertThat(deepCopy.getAddress().getCountry())
                .isNotEqualTo(pm.getAddress().getCountry());
    }

    // Jackson
    @Test
    public void whenModifyingOriginalObject_thenJacksonCopyShouldNotChange() throws IOException {
        Address address = new Address("Downing St 10", "London", "England");
        User pm = new User("Prime", "Minister", address);
        ObjectMapper objectMapper = new ObjectMapper();

        User deepCopy = objectMapper
                .readValue(objectMapper.writeValueAsString(pm), User.class);

        address.setCountry("Great Britain");

        assertThat(deepCopy.getAddress().getCountry())
                .isNotEqualTo(pm.getAddress().getCountry());
    }
}
