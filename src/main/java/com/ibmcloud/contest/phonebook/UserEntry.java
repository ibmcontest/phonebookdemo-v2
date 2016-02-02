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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "userentry")
@XmlRootElement(name = "userEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserEntry {

    public UserEntry() {
        super();
    }

    public UserEntry(final String key) {
        this.key = key;
    }

    @Id
    // Primary Key
    @Column(name = "userkey")
    @XmlElement(name = "userkey")
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

}
