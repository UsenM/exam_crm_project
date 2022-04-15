package dao.daoutil;

import dao.CourseDao;
import dao.CourseFormatDao;
import dao.CrudDao;
import dao.impl.CourseDaoImpl;
import dao.impl.CourseFormatDaoImpl;

public abstract class DaoContext {


    static {

        try {

            Log.message("Loading driver...");
            Class.forName("org.postgresql.Driver");
            Log.message("Driver loaded");

        } catch (Exception e) {

            Log.message("Driver loading failed.");
            e.printStackTrace();
        }
    }

    private static CourseDao courseDao;
    private static CourseFormatDao courseFormatDao;


    public static CrudDao<?> autowired(String qualifier) {

        switch (qualifier) {

            case "CourseFormatDao":
                return getCourseFormatDao();

            case "CourseDao":
                return getCourseDaoSQL();
            default:
                throw new RuntimeException("Didn't find bean for autowiring: " + qualifier);

        }
    }


    public static CourseDao getCourseDaoSQL() {

        if (courseDao == null)
            courseDao = new CourseDaoImpl();
        return courseDao;

    }


    private static CourseFormatDao getCourseFormatDao() {

        if (courseFormatDao == null)
            courseFormatDao = new CourseFormatDaoImpl();
        return courseFormatDao;

    }


}
