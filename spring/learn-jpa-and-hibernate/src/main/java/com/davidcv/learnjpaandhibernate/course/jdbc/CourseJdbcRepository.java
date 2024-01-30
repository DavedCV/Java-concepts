package com.davidcv.learnjpaandhibernate.course.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseJdbcRepository {

    private static String testInsertQuery = """
                                            INSERT INTO course (id, name, author) VALUES
                                            (1, 'Learn AWS', 'In28minutes');
                                            """;
    private JdbcTemplate jdbcTemplate;

    public CourseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert() {
        jdbcTemplate.update(testInsertQuery);
    }
}

