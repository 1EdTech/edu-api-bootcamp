package org.oneedtech.eduapi.bootcamp.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.oneedtech.eduapi.bootcamp.api.CourseOfferingsApiDelegate;
import org.oneedtech.eduapi.bootcamp.model.CourseOffering;
import org.oneedtech.eduapi.bootcamp.model.CourseOffering.RecordStatusEnum;
import org.oneedtech.eduapi.bootcamp.model.CourseOffering.RegistrationStatusEnum;
import org.oneedtech.eduapi.bootcamp.model.IdentifierEntry;
import org.oneedtech.eduapi.bootcamp.model.LanguageTypedString;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import jakarta.annotation.PostConstruct;

@Component
public class CourseOfferingApiDelegateImpl implements CourseOfferingsApiDelegate {
  private List<CourseOffering> courseOfferings;
  private Integer COURSE_OFFERING_COUNT = 50;
  private Faker faker = new Faker();

  @PostConstruct
  void initCourseOfferingRepository() {
    courseOfferings =
        IntStream.range(0, COURSE_OFFERING_COUNT)
            .mapToObj(
                i ->
                    new CourseOffering.Builder()
                        .sourcedId(UUID.randomUUID().toString())
                        .offeringType("standard")
                        .course(UUID.randomUUID().toString())
                        .recordLanguage("nl-NL")
                        .title(List.of(
                            new LanguageTypedString.Builder().recordLanguage("nl-NL").value("Hij maakte kennis met de informatica").build(),
                            new LanguageTypedString.Builder().recordLanguage("ca-ES").value("Introducció a la informàtica").build()
                        ))
                        .description(List.of(
                            new LanguageTypedString.Builder().recordLanguage("nl-NL").value("Hij maakte kennis met de informatica").build(),
                            new LanguageTypedString.Builder().recordLanguage("ca-ES").value("Introducció a la informàtica").build()
                        ))
                        .primaryCode(new IdentifierEntry.Builder()
                            .identifier("INF-101")
                            .build())
                        .organization(UUID.randomUUID().toString())
                        .organizationCode("CS-" + faker.number().digits(5))
                        .academicSession(UUID.randomUUID().toString())
                        .academicSessionCode("FALL2025")
                        .offeringFormat("online")
                        .registrationStatus(RegistrationStatusEnum.OPEN)
                        .startDate(OffsetDateTime.now().plus(30, java.time.temporal.ChronoUnit.DAYS))
                        .endDate(OffsetDateTime.now().plus(180, java.time.temporal.ChronoUnit.DAYS))
                        .recordStatus(RecordStatusEnum.ACTIVE)
                        .dateLastModified(OffsetDateTime.now())
                        .build())
            .collect(Collectors.toList());
  }

    @Override
    public ResponseEntity<List<CourseOffering>> getAllCourseOfferings(Integer limit, Integer offset, String sort,
            String orderBy, String filter) {
    return ResponseEntity.ok(courseOfferings);
    }

    @Override
    public ResponseEntity<CourseOffering> getCourseOfferingById(String id) {
        return courseOfferings.stream()
            .filter(courseOffering -> courseOffering.getSourcedId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
