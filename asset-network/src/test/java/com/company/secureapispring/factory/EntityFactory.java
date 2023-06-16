package com.company.secureapispring.factory;

import com.github.javafaker.Faker;

import at.srfg.iasset.network.entities.Enterprise;

public final class EntityFactory {
    private static Faker faker = Faker.instance();

    public static Faker getFaker() {
        return faker;
    }

    private EntityFactory() {}

    public static EntityBuilder<Enterprise> enterprise() {
        return EntityBuilder
                .of(Enterprise::new)
                .with(Enterprise::setDescription, faker.company().industry())
                .with(Enterprise::setUri, faker.company().url())
                .with(Enterprise::setDomain, faker.country().countryCode2())
                .with(Enterprise::setName, faker.company().name());
        	
    }

//    public static EntityBuilder<StateProvince> stateProvince() {
//        return EntityBuilder
//                .of(StateProvince::new)
//                .with(StateProvince::setAbbreviation, faker.address().stateAbbr())
//                .with(StateProvince::setName, faker.address().state());
//    }
//
//    public static EntityBuilder<Customer> customer() {
//        return EntityBuilder
//                .of(Customer::new)
//                .with(Customer::setFirstName, faker.name().firstName())
//                .with(Customer::setLastName, faker.name().lastName())
//                .with(Customer::setEmail, faker.internet().emailAddress())
//                .with(Customer::setAddress, faker.address().streetAddressNumber())
//                .with(Customer::setAddress2, faker.address().secondaryAddress())
//                .with(Customer::setPostalCode, faker.address().zipCode());
//    }
}
