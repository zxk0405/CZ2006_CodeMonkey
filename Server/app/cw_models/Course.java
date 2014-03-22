package cw_models;

import models.Exam;
import play.db.ebean.Model;
import utils.CMException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Course extends Model{
    @Id
    @Column(name = "course_id")
    private Integer courseId;
    private String courseCode;
    private String title;
    private Integer questionNo;
    @OneToMany(mappedBy="course")
    private List<Question> questionSet = new ArrayList<Question>();
    @OneToMany(mappedBy="course")
    private List<TimeSlot> availableSlots = new ArrayList<TimeSlot>();
    @OneToMany(mappedBy="course")
    private List<Registration> registrationList = new ArrayList<Registration>();
    @Transient
    private List<Exam> examList = new ArrayList<Exam>();

    public static Finder<Integer, Course> find = new Finder<Integer, Course>(
            "cw", Integer.class,Course.class
    );

    public Course(){ }

    public List<Exam> getExamList(){
        return Exam.find.where().eq("courseId",courseId).findList();
    }
    public Integer getCourseId(){
        return courseId;
    }
    public String getCourseCode(){
        return courseCode;
    }
    public String getTitle(){
        return title;
    }
    public Integer getQuestionNo(){return questionNo;}
    public List<Question> getQuestionSet(){
        return Question.find.where().eq("course",this).findList();
    }
    public List<TimeSlot> getAvailableSlots(){
        return TimeSlot.find.where().eq("course",this).findList();
    }
    public List<Registration> getRegistrationList(){
        return Registration.find.where().eq("course",this).findList();
    }

    public void setCourseCode(String courseCode) throws CMException{
        if(!courseCode.matches("^[a-zA-Z0-9]{5,7}$")){
            throw new CMException("CourseCode should be 5-7 alphanumeric");
        }
        if(Course.find.where("courseCode = :courseCode").setParameter("courseCode",courseCode).findRowCount()!=0){
            throw new CMException("Course " + courseCode + " has been added already.");
        }
        this.courseCode = courseCode.toUpperCase();
    }
    public void setTitle(String title) throws CMException{
        if(title == ""){
            throw new CMException("Please enter the course title.");
        }
        if(title.length()>30){
            throw new CMException("Title length should be less than 30");
        }
        this.title = title;
    }
    public void setQuestionNo(Integer questionNo) throws CMException{
        if(questionNo <= 0){
            throw new CMException("Please enter a positive question number.");
        }
        this.questionNo = questionNo;
    }

    public static Course byId(Integer courseId){
        return Course.find.where().eq("courseId",courseId).findUnique();
    }
}
