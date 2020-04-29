package com.kor.switter.domain;

import com.kor.switter.domain.util.MessageHelper;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "please fill the message")
    @Length(max = 255, message = "to long")
    private String text;
    @Length(max = 255, message = "to long")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    private Timestamp timeOfCreation;

    @ManyToMany
    @JoinTable(name = "message_likes",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public Message() {
    }

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;

        //,    private Timestamp dateOfCreation;
        //    date_of_creation timestamp without time zone not null
        //spring.jpa.properties.hibernate.jdbc.time_zone=UTC
    }

    //GETTER
    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    public User getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    public String getFilename() {
        return filename;
    }

    public String getShoreFilename() {
        return filename.substring(37, filename.length());
    }

    public Set<User> getLikes() {
        return likes;
    }

    public Timestamp getTimeOfCreation() {
        return timeOfCreation;
    }

    //SETTER
    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public void setTimeOfCreation(Timestamp timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }
}
