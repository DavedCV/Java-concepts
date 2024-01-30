package com.davidcv.learnjpaandhibernate.course.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseJdbcRepository {

    private static String testInsertQuery = """
                                            INSERT INTO course (id, name, author) VALUES
                                            (?, ?, ?);
                                            """;
    private static String testDeleteQuery = """
                                            DELETE FROM course WHERE id = ?;
                                            """;
    private static String testSelectQuery = """
                                            SELECT * FROM course WHERE id = ?;
                                            """;
    private JdbcTemplate jdbcTemplate;

    public CourseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Course course) {
        jdbcTemplate.update(testInsertQuery, course.getId(), course.getName(), course.getAuthor());
    }

    public void delete(long id) {
        jdbcTemplate.update(testDeleteQuery, id);
    }

    public Course select(long id) {

        // ResultSet -> Bean
        return jdbcTemplate.queryForObject(testSelectQuery, new BeanPropertyRowMapper<>(Course.class), id);
    }
}

