package entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@FieldDefaults(level = AccessLevel.PROTECTED)

public class BaseEntity {
    protected Long id;
    protected LocalDateTime dateCreated;


    public BaseEntity() {
    }


    public BaseEntity(Long id, LocalDateTime dateCreated) {
        this.id = id;
        this.dateCreated = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
