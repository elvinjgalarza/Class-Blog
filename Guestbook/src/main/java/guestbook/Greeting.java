
package guestbook;

import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;


// The objectify-appengine library (objectify for short) makes
// using the Google AppEngine datastore much easier.
// Specifically, it allows developers to add objects
// directly to the datastore, and to retrieve objets
// from the datestore by specifying their class and,
// optionally, field values. 


// Entity is defined to tell Objectify what part of the code is in the application
@Entity
public class Greeting implements Comparable<Greeting> {
	@Parent Key parent;
	
    //Entities must have one field annotated w/ @ID
    //The actual name of the field is irrelevant and can be renamed at
    //any time, even after data is persisted. This value becomes
    //part of the Key which identifies an entity.
    @Id Long id;
    
     User user;
     String content;
     Date date;
     String title;
     
    
    
    //There must be a no-arg constructor 
    private Greeting() {}
    
    public Greeting(User user, String content, Key parent, String title) {
        this.parent = parent;
    	this.user = user;
        this.content = content;
        date = new Date();
        this.title = title;
    }
    
    //Getters and Setters
    public String getTitle() {
    	return title;
    }
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
    	this.user = user;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
    	this.content = content;
    }
    
    public Date getDate() {
    	return date;
    }
    public void setDate(Date date) {
    	this.date = date;
    }
    
    public Key getParent() {
    	return parent;
    }
    public void setParent(Key parent) {
    	this.parent = parent;
    }
    
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }
    

    @Override
    public int compareTo(Greeting other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
     }
}