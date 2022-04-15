package dao.impl;

import dao.CourseDao;
import dao.daoutil.Log;
import entities.Course;
import entities.CourseFormat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDaoImpl implements CourseDao {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Course savedCourse = null;
    private CourseFormat courseFormat = null;

    public CourseDaoImpl() {

        try {
            Log.info(this.getClass().getSimpleName() + " CourseDaoImpl()", Connection.class.getSimpleName(), "Establishing connection.");
            connection = getConnection();

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_courses(" +
                    "id                  BIGSERIAL, " +
                    "course_name         VARCHAR(50)  NOT NULL, " +
                    "price               MONEY        NOT NULL, " +
                    "date_created        TIMESTAMP    NOT NULL DEFAULT NOW(), " +
                    "course_format_id    BIGINT NOT NULL, " +
                    "" +
                    "CONSTRAINT pk_course_id PRIMARY KEY(id)," +
                    "CONSTRAINT fk_course_format_id FOREIGN KEY (course_format_id) " +
                    "REFERENCES tb_course_format(id)" +
                    ");";


            preparedStatement = connection.prepareStatement(ddlQuery);
            preparedStatement.execute();


        } catch (Exception e) {

            Log.error(this.getClass().getSimpleName() + " CourseFormatDaoImpl()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();

        } finally {
            close(preparedStatement);
            close(connection);

        }
    }


    @Override
    public List<Course> findAll() {

        List<Course> list = new ArrayList<>();

        try {

            Log.info(this.getClass().getSimpleName() + "findAll()", Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();


            String readQuery = "SELECT f.*, c.* " +
                    "FROM tb_course_format AS f " +
                    "JOIN tb_courses AS c " +
                    "ON f.id = c.course_format_id  " +
                    "ORDER BY f.course_format;";


            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                courseFormat = getCourseFormat();
                savedCourse = getSavedCourse();

                list.add(savedCourse);
            }


            return list;


        } catch (Exception e) {

            Log.error(this.getClass().getSimpleName() + "findAll()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.getStackTrace();

        }
        return null;
    }


    @Override
    public Optional<Course> save(Course course) {
        try {

            Log.info(this.getClass().getSimpleName() + " save()", Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();

            String createQuery = "INSERT INTO tb_courses(" +
                    "course_name, price, date_created, course_format_id) " +
                    "VALUES(?, MONEY(?), ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement = connection.prepareStatement(createQuery);

            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, String.valueOf(course.getPrice()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(course.getDateCreated()));
            preparedStatement.setLong(4, course.getCourseFormat().getId());


            preparedStatement.execute();
            close(preparedStatement);


            String readQuery = "SELECT f.*, c.* " +
                    "FROM tb_course_format AS f " +
                    "JOIN tb_courses AS c " +
                    "ON f.id = c.course_format_id  " +
                    "ORDER BY f.course_format;";


            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            courseFormat = getCourseFormat();
            savedCourse = getSavedCourse();


        } catch (Exception e) {
            Log.error(this.getClass().getSimpleName() + " save()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();

        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }


        return Optional.of(savedCourse);
    }

    private Course getSavedCourse() throws SQLException {
        return Course.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("course_name"))
                .price(Double.parseDouble(resultSet.getString("price").replaceAll("[^\\d\\.]+", "")) / 100)
                .dateCreated(resultSet.getTimestamp("date_created").toLocalDateTime())
                .courseFormat(courseFormat)
                .build();
    }


    private CourseFormat getCourseFormat() throws SQLException {
        return CourseFormat.builder()
                .id(resultSet.getLong("id"))
                .format(resultSet.getString("course_format"))
                .isOnline(resultSet.getBoolean("is_online"))
                .lessonDuration(resultSet.getTime("lesson_duration").toLocalTime())
                .courseDurationWeeks(resultSet.getInt("course_duration_weeks"))
                .dateCreated(resultSet.getTimestamp("date_created").toLocalDateTime())
                .lessonsPerWeek(resultSet.getInt("lessons_per_week"))
                .build();
    }

}

