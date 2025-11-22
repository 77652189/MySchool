    package com.nan.myschool.entity;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    /**
     * CourseSection实体类 - 课程章节/班级
     * 一门课程可以有多个章节，由不同教师在不同时间地点授课
     */
    @Entity
    @Table(name = "course_section")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CourseSection {

        /**
         * 章节ID - 主键，自动生成
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer sectionId;

        /**
         * 关联的课程 - 多对一关系
         * 多个章节属于同一门课程
         */
        @ManyToOne
        @JoinColumn(name = "course_id", nullable = false)
        private Course course;

        /**
         * 授课教师 - 多对一关系
         * 一个教师可以教授多个章节
         */
        @ManyToOne
        @JoinColumn(name = "instructor_id", nullable = false)
        private Instructor instructor;

        /**
         * 教室
         */
        @Column(length = 50)
        private String room;

        /**
         * 上课时间安排
         * 例如: "Mon & Wed 9:00-10:15 AM"
         */
        @Column(length = 100)
        private String schedule;

        /**
         * 最大容量
         */
        @Column(nullable = false)
        private Integer capacity;

        @Override
        public String toString() {
            return "第" + sectionId + "章节 (教室:" + room + ")";
        }


        // 注意：enrolledStudents (List) 不需要在这里定义
        // 它通过Enrollment表查询获得
    }