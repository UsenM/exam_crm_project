package dao.impl;

import dao.CourseFormatDao;
import dao.daoutil.Log;
import entities.CourseFormat;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseFormatDaoImpl implements CourseFormatDao {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private CourseFormat savedCourseFormat = null;


    public CourseFormatDaoImpl() {


        try {
            Log.info(this.getClass().getSimpleName() + " CourseFormatDaoImpl()", Connection.class.getSimpleName(), "Establishing connection.");
            connection = getConnection();

            String ddlQuery = "CREATE TABLE IF NOT EXISTS tb_course_format(" +
                    "id BIGSERIAL, " +
                    "course_format VARCHAR(50) NOT NULL, " +
                    "course_duration_weeks INT NOT NULL, " +
                    "lesson_duration TIME NOT NULL, " +
                    "lessons_per_week INT NOT NULL, " +
                    "is_online BOOLEAN NOT NULL, " +
                    "date_created TIMESTAMP NOT NULL DEFAULT NOW(), " +
                    "" +
                    "CONSTRAINT pk_course_format_id PRIMARY KEY(id), " +
                    "CONSTRAINT course_duration_weeks_negative_or_zero CHECK (course_duration_weeks > 0), " +
                    "CONSTRAINT lesson_per_week_negative_or_zero CHECK (lessons_per_week > 0)" +
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
    public Optional<CourseFormat> save(CourseFormat courseFormat) {

        try {

            Log.info(this.getClass().getSimpleName() + " save()", Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();

            String createQuery = "INSERT INTO tb_course_format(" +
                    "course_format, course_duration_weeks, lesson_duration, lessons_per_week, is_online, date_created ) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(createQuery);

            preparedStatement.setString(1, courseFormat.getFormat());
            preparedStatement.setInt(2, courseFormat.getCourseDurationWeeks());
            preparedStatement.setTime(3, Time.valueOf((courseFormat.getLessonDuration())));
            preparedStatement.setInt(4, courseFormat.getLessonsPerWeek());
            preparedStatement.setBoolean(5, courseFormat.isOnline());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(courseFormat.getDateCreated()));

            preparedStatement.execute();
            close(preparedStatement);

            String readQuery = "SELECT * FROM tb_course_format ORDER BY id DESC LIMIT 1";

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();


            savedCourseFormat = getSavedCourseFormat();


        } catch (Exception e) {
            Log.error(this.getClass().getSimpleName() + " save()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();

        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }


        return Optional.of(savedCourseFormat);
    }


    @Override
    public List<CourseFormat> findAll() {

        List<CourseFormat> list = new ArrayList<>();

        try {

            Log.info(this.getClass().getSimpleName() + "findAll()", Connection.class.getSimpleName(), "Establishing connection");
            connection = getConnection();

            String readQuery = "SELECT * FROM tb_course_format;";

            preparedStatement = connection.prepareStatement(readQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                savedCourseFormat = getSavedCourseFormat();

                list.add(savedCourseFormat);
            }

            return list;


        } catch (Exception e) {

            Log.error(this.getClass().getSimpleName() + "findAll()", e.getStackTrace()[0].getClass().getSimpleName(), e.getMessage());
            e.getStackTrace();

        }
        return null;
    }


    private CourseFormat getSavedCourseFormat() throws SQLException {
        return CourseFormat.builder()
                .id(resultSet.getLong("id"))
                .format(resultSet.getString("course_format"))
                .courseDurationWeeks(resultSet.getInt("course_duration_weeks"))
                .lessonDuration(LocalTime.parse(resultSet.getString("lesson_duration")))
                .lessonsPerWeek(resultSet.getInt("lessons_per_week"))
                .isOnline(resultSet.getBoolean("is_online"))
                .dateCreated(resultSet.getTimestamp("date_created").toLocalDateTime())
                .build();
    }
}
