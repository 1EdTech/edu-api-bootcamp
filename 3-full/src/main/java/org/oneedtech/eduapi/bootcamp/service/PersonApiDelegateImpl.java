package org.oneedtech.eduapi.bootcamp.service;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.oneedtech.eduapi.bootcamp.api.PersonsApiDelegate;
import org.oneedtech.eduapi.bootcamp.model.OptionallyTypedAddress;
import org.oneedtech.eduapi.bootcamp.model.OptionallyTypedPhone;
import org.oneedtech.eduapi.bootcamp.model.Person;
import org.oneedtech.eduapi.bootcamp.model.PersonName;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonApiDelegateImpl implements PersonsApiDelegate {
  private List<Person> persons;
  private Integer PERSON_COUNT = 50;
  private Faker faker = new Faker();

  @PostConstruct
  void initPersonRepository() {
    persons =
        IntStream.range(0, PERSON_COUNT)
            .mapToObj(
                i ->
                    new Person.Builder()
                        .sourcedId(UUID.randomUUID().toString())
                        .dateOfBirth(
                            faker
                                .date()
                                .birthday()
                                .toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate())
                        .legalName(
                            new PersonName.Builder()
                                .givenName(faker.name().firstName())
                                .familyName(faker.name().lastName())
                                .build())
                        .recordLanguage("en-US")
                        .placeOfBirth(faker.address().cityName())
                        .countryOfBirth(faker.address().country())
                        .primaryPhone(
                            new OptionallyTypedPhone.Builder()
                                .phone(faker.phoneNumber().phoneNumber())
                                .build())
                        .primaryAddress(
                            new OptionallyTypedAddress.Builder()
                                .streetAddress(faker.address().streetAddress())
                                .addressLocality(faker.address().cityName())
                                .postalCode(faker.address().zipCode())
                                .addressRegion(faker.address().state())
                                .addressCountryCode(faker.address().countryCode())
                                .addressCountry(faker.address().country())
                                .build())
                        .build())
            .collect(Collectors.toList());
  }

  @Override
  public ResponseEntity<List<Person>> getAllPersons(
      Integer limit, Integer offset, String sort, String orderBy, String filter) {
    return ResponseEntity.ok(persons);
  }

  @Override
  public ResponseEntity<Person> getPersonById(String id) {
    return persons.stream()
        .filter(p -> p.getSourcedId().equals(id))
        .findFirst()
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
