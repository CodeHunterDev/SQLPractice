package randomappsinc.com.sqlpractice.database;

import randomappsinc.com.sqlpractice.database.models.Question;
import randomappsinc.com.sqlpractice.utils.TutorialServer;

// This class contains the questions our app contains
public class QuestionServer {

    private static QuestionServer instance;

    private static int[][] questionTablePairings =
            {{0}, {0}, {0}, {0}, {0},
             {0}, {0}, {0}, {0}, {0},
             {0}, {0}, {1}, {1, 2}, {1, 2},
             {0}, {0}, {1}, {0}, {2},
             {0}, {0}, {0}, {0}, {1, 2},
             {1}, {0}, {0}, {0}, {0},
             {0}, {0}};

    private static String[][] questionIdeaPairings =
           {{TutorialServer.INTRO},
            {TutorialServer.INTRO},
            {TutorialServer.DISTINCT},
            {TutorialServer.WHERE},
            {TutorialServer.GROUP_BY},
            {TutorialServer.MAX},
            {TutorialServer.MAX},
            {TutorialServer.ORDER_BY, TutorialServer.LIMIT},
            {TutorialServer.MIN},
            {TutorialServer.WHERE},
            {TutorialServer.LIKE},
            {TutorialServer.DISTINCT, TutorialServer.ORDER_BY, TutorialServer.LIMIT},
            {TutorialServer.ALIASES, TutorialServer.SUBQUERIES, TutorialServer.AND_OR},
            {TutorialServer.INNER_JOIN},
            {TutorialServer.INNER_JOIN, TutorialServer.AND_OR},
            {TutorialServer.AVG},
            {TutorialServer.INTRO},
            {TutorialServer.SQLITE_METADATA},
            {TutorialServer.MIN, TutorialServer.SUBQUERIES},
            {TutorialServer.COUNT, TutorialServer.DISTINCT},
            {TutorialServer.WHERE, TutorialServer.AND_OR},
            {TutorialServer.WHERE, TutorialServer.AND_OR},
            {TutorialServer.MAX},
            {TutorialServer.WHERE, TutorialServer.SUBQUERIES},
            {TutorialServer.INNER_JOIN, TutorialServer.AND_OR},
            {TutorialServer.COALESCE, TutorialServer.DISTINCT},
            {TutorialServer.SUM, TutorialServer.GROUP_BY},
            {TutorialServer.AVG, TutorialServer.GROUP_BY},
            {TutorialServer.MAX, TutorialServer.GROUP_BY},
            {TutorialServer.DISTINCT, TutorialServer.ORDER_BY, TutorialServer.LIMIT, TutorialServer.SUBQUERIES},
            {TutorialServer.GROUP_BY, TutorialServer.COUNT},
            {TutorialServer.GROUP_BY, TutorialServer.COUNT, TutorialServer.ORDER_BY, TutorialServer.LIMIT}};

    // Questions stored here in this ghetto hard-coded array
    private static String[] questions =
            {"Write a query that outputs all of the contents of the table.",
                    "Write a query that returns the names of all the professors in the table.",
                    "Write a query that returns all departments in the table (no duplicates).",
                    "Write a query that returns the number of professors whose salary is greater than 150000.",
                    "Write a query that returns all departments and their aggregate salaries (in this column order).",
                    "Write a query that returns the name and salary (in this column order) of the professor with the " +
                            "highest salary.",
                    "Write a query that returns the name and salary (in this column order) of the professor with the " +
                            "highest salary in the \"Computer Science\" department.",
                    "Write a query that returns the name and salaries (in this column order) of the Top 5 highest earning" +
                            " professors.",
                    "Write a query that returns the name and salary (in this column order) of the lowest earning professor.",
                    "Write a query that returns the department Professor \"Zaniolo\" works in.",
                    "Write a query that returns all the professor names that begin with the letter 'C'.",
                    "Write a query that returns the third highest salary in the table. " +
                    "Duplicate salaries count as one. For example, if you have 20, 20, 10, and 5, 10 is the second highest salary.",
                    "Write a query that returns the first and last name (in this column order) of people who share their last name " +
                            "with someone that has also checked out a book.",
                    "Write a query that returns the first and last name of all people who checked out a book by Terry Crews.",
                    "Write a query that returns the first and last names of all people who checked out \"To Kill a Mockingbird\"" +
                            " by Harper Lee.",
                    "Write a query that returns the average salary of the professors in the table.",
                    "Each professor in the Computer Science department just got a 10,000 dollar raise. " +
                            "Write a query that gives the names and new salaries (in this column order) of the " +
                            "Computer Science professors.",
                    "Write a query that outputs the SQL statement used to create the above table.",
                    "Write a query that returns the name and salary (in this column order) of all professors who make more than " +
                            "4 times as much as the lowest paid professor.",
                    "Write a query that returns the amount of unique authors who have written books in this table.",
                    "Write a query that returns the name and salary (in this column order) of professors who earn " +
                            "between 120000 and 250000 a year (inclusive).",
                    "Write a query that returns all professors (all columns) who either work in the \"Anthropology\" " +
                            "department or makes more than 150000 a year.",
                    "Write a query that returns the highest salary in the \"Computer Science\" department.",
                    "Write a query that returns the amount of professors who earn more than twice as much as the " +
                            "lowest paid professor in the \"Political Science\" department.",
                    "Write a query that returns the names of all books checked out by Justin (first name) Lee (last name).",
                    "Write a query that returns the full names (full name is first name followed by a space " +
                            "and then last name) of everyone who has checked out a book. No duplicates!",
                    "Write a query that returns the department that makes the most along with that " +
                            "department's total aggregate salary.",
                    "Write a query that returns the department with the highest average salary " +
                            "along with that average salary.",
                    "Write a query that returns the name, department, and salary (in this column order) " +
                            "of each professor who is the highest earning in their department.",
                    "Write a query that returns the name and salary (in this column order) " +
                            "of each professor who is either a top 3 or bottom 3 earner (include ties).",
                    "Write a query that returns every department and the number of professors in each one " +
                            "(in this column order).",
                    "Write a query that returns the department with the most professors " +
                            "and that amount (in this column order)."};

    private Question[] allQuestions = new Question[questions.length];

    private QuestionServer () {
        for (int i = 0; i < questions.length; i++) {
            allQuestions[i] = new Question(questions[i], questionTablePairings[i], questionIdeaPairings[i]);
        }
    }

    public static QuestionServer getQuestionServer() {
        if (instance == null) {
            instance = new QuestionServer();
        }
        return instance;
    }

    public static int getNumQuestions() {
        return questions.length;
    }

    public Question getQuestion(int position) {
        return allQuestions[position];
    }
}
