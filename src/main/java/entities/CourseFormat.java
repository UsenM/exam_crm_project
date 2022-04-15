package entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public final class CourseFormat extends BaseEntity {

    String format;
    int courseDurationWeeks;
    LocalTime lessonDuration;
    int lessonsPerWeek;
    boolean isOnline;

    @Builder
    public CourseFormat(Long id, LocalDateTime dateCreated, String format, int courseDurationWeeks, LocalTime lessonDuration, int lessonsPerWeek, boolean isOnline) {
        super(id, dateCreated);
        this.format = format;
        this.courseDurationWeeks = courseDurationWeeks;
        this.lessonDuration = lessonDuration;
        this.lessonsPerWeek = lessonsPerWeek;
        this.isOnline = isOnline;
    }


    @Override
    public String toString() {
        return "CourseFormat{" +
                "id=" + id +
                ", dateCreated=" + dateCreated.format(DateTimeFormatter.ISO_DATE) +
                ", format='" + format + '\'' +
                ", courseDurationWeeks=" + courseDurationWeeks +
                ", lessonDuration=" + lessonDuration +
                ", lessonsPerWeek=" + lessonsPerWeek +
                ", isOnline=" + isOnline +
                '}';
    }
}
