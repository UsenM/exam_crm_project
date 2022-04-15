import dao.CourseDao;
import dao.CourseFormatDao;
import dao.daoutil.DaoContext;
import dao.daoutil.Log;
import entities.Course;
import entities.CourseFormat;

import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        /** Course Format Creating */
        CourseFormatDao courseFormatDao = (CourseFormatDao) DaoContext.autowired("CourseFormatDao");

        CourseFormat onlineMeetup = CourseFormat.builder()
                .format("ONLINE MEETUP")
                .lessonsPerWeek(3)
                .lessonDuration(LocalTime.of(1, 30))
                .courseDurationWeeks(24)
                .isOnline(true).
                build();
        Log.message("Saved course format: " + courseFormatDao.save(onlineMeetup));

        CourseFormat onlineMeetUpFromDB = courseFormatDao.findAll().get(courseFormatDao.findAll().size() - 1);


        CourseFormat bootcamp = CourseFormat.builder()
                .format("BOOTCAMP")
                .lessonsPerWeek(5)
                .lessonDuration(LocalTime.of(3, 0))
                .courseDurationWeeks(12)
                .isOnline(false).
                build();
        Log.message("Saved course format: " + courseFormatDao.save(bootcamp));
        CourseFormat bootcampFromDB = courseFormatDao.findAll().get(courseFormatDao.findAll().size() - 1);


        CourseFormat ordinary = CourseFormat.builder()
                .format("ORDINARY")
                .lessonsPerWeek(4)
                .lessonDuration(LocalTime.of(2, 30))
                .courseDurationWeeks(18)
                .isOnline(false).
                build();
        Log.message("Saved course format: " + courseFormatDao.save(ordinary));
        CourseFormat ordinaryFromDB = courseFormatDao.findAll().get(courseFormatDao.findAll().size() - 1);


        /** Course  Creating */
        CourseDao courseDao = (CourseDao) DaoContext.autowired("CourseDao");
        Course java = Course.builder()
                .name("JAVA")
                .price(15000)
                .courseFormat(bootcampFromDB)
                .build();
        courseDao.save(java);

        Course sql = Course.builder()
                .name("SQL")
                .price(8000)
                .courseFormat(ordinaryFromDB)
                .build();
        courseDao.save(sql);

        Course python = Course.builder()
                .name("PYTHON")
                .price(12000)
                .courseFormat(onlineMeetUpFromDB)
                .build();
        courseDao.save(python);

        courseDao.findAll().forEach(System.out::println);
    }
}
