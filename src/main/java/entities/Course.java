package entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor

public final class Course extends BaseEntity {
    String name;
    double price;
    CourseFormat courseFormat;


    @Builder
    public Course(Long id, LocalDateTime dateCreated, String name, double price, CourseFormat courseFormat) {
        super(id, dateCreated);
        this.name = name;
        this.price = price;
        this.courseFormat = courseFormat;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", dateCreated=" + dateCreated.format(DateTimeFormatter.ISO_DATE) +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", courseFormat=" + courseFormat +
                '}';
    }
}
