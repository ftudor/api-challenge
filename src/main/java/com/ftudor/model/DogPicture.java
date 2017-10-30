package com.ftudor.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Picture POJO.
 *
 * Holds information about dog pictures
 * Uses annotations from java.persistence to be used via Hibernate
 *
 * @author Florin Tudor
 */

@Entity
@Table(name = "dog_picture")
public class DogPicture {
    private int id;        //unique key
    private String url;    //unique as well
    private String description;

    //vote info - concurrent access
    private AtomicInteger votes = new AtomicInteger(0);

    //dog breed
    private DogBreed breed;

    //image info
    private int dpi;
    private int resolutionX;
    private int resolutionY;

    //use auto-generated primary keys; we assume they are positive starting from 1
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public AtomicInteger getVotes() {
        return votes;
    }

    public void setVotes(final AtomicInteger votes) {
        this.votes = votes;
    }

    public DogBreed getBreed() {
        return breed;
    }

    public void setBreed(final DogBreed breed) {
        this.breed = breed;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(final int dpi) {
        this.dpi = dpi;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(final int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(final int resolutionY) {
        this.resolutionY = resolutionY;
    }

    //equals() ans hashCode() overriden for DB operations, using in Sets, etc
    //purpose is to determine uniqueness based on a few key fields

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(url).
                append(description).
                append(breed).
                append(votes).
                toHashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        DogPicture other = (DogPicture) obj;

        return new EqualsBuilder().
                append(url, other.url).
                append(description, other.description).
                append(breed, other.breed).
                append(votes, other.votes).
                isEquals();
    }
}
