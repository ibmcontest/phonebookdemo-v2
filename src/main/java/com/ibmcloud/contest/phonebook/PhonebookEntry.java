/**
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibmcloud.contest.phonebook;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "phonebook")
@XmlRootElement(name = "phonebookEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhonebookEntry {

    // Default constructor
    public PhonebookEntry() {
        super();
    }

    // Constructor without favorite. Sets to false by default
    public PhonebookEntry(final String title, final String firstName, final String lastName,
            final String phoneNumber, final String email) {
        super();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.favorite = false;
        this.userkey = null;
    }

    // Constructor with favorite included.
    public PhonebookEntry(final String title, final String firstName, final String lastName,
            final String phoneNumber, final String email, final Boolean favorite) {
        super();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.favorite = favorite;
        this.userkey = null;
    }

    public PhonebookEntry(final String title, final String firstName, final String lastName,
            final String phoneNumber, final String email, final String userkey) {
        super();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.favorite = false;
        this.userkey = userkey;
    }

    public PhonebookEntry(final String title, final String firstName, final String lastName,
            final String phoneNumber, final String email, final String userkey, final Boolean favorite) {
        super();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.favorite = favorite;
        this.userkey = userkey;
    }

    @Id
    // Primary Key
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "id")
    long id;

    @Basic
    @Column(name = "TITLE")
    @XmlElement(name = "title")
    String title;

    @Basic
    @Column(name = "FIRSTNAME")
    @XmlElement(name = "firstName")
    String firstName;

    @Basic
    @Column(name = "LASTNAME")
    @XmlElement(name = "lastName")
    String lastName;

    @Basic
    @Column(name = "PHONENUMBER")
    @XmlElement(name = "phoneNumber")
    String phoneNumber;

    @Basic
    @Column(name = "USERKEY")
    @XmlTransient
    @ApiModelProperty(hidden = true)
    String userkey;

    @Basic
    @Column(name = "EMAIL")
    @XmlElement(name = "email")
    String email;

    @Basic
    @Column(name = "FAVORITE")
    @XmlElement(name = "favorite")
    Boolean favorite = false;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserKey(final String userkey) {
        this.userkey = userkey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(final Boolean favorite) {
        this.favorite = favorite;
    }

    // Simple equals method to check if title, name, and phone number are the same as "other"
    public boolean equals(final PhonebookEntry other) {
        return (this.title == other.getTitle() && this.firstName == other.getFirstName()
                && this.lastName == other.getLastName() && this.phoneNumber == other.getPhoneNumber()
                && this.email == other.getEmail());
    }

    public void fillNulls() {
        if (title == null) {
            title = ""; //$NON-NLS-1$
        }
        if (firstName == null) {
            firstName = ""; //$NON-NLS-1$
        }
        if (lastName == null) {
            lastName = ""; //$NON-NLS-1$
        }
        if (phoneNumber == null) {
            phoneNumber = ""; //$NON-NLS-1$
        }
        if (email == null) {
            email = ""; //$NON-NLS-1$
        }
    }

}
