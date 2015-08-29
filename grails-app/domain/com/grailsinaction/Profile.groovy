package com.grailsinaction

class Profile {

    static belongsTo = [user: User]
    byte[] photo
    String timezone
    String email
    String fullName
    String homepage
    String country
    String jabberAddress
    String bio
    static constraints = {
        fullName blank: false
        email email: true, nullable: false
        timezone nullable: true
        photo nullable: true, maxSize: 1024*1024
        homepage url: true, nullable: true
        country nullable: true
        jabberAddress url: true, nullable:true
        bio nullable: true
    }

}
