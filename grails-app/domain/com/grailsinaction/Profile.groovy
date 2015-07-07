package com.grailsinaction

class Profile {

    User user
    byte[] photo
    String timezone
    String email
    String fullName
    String homepage
    String country
    String jabberAddress
    static constraints = {
        fullName blank: false
        email email: true, nullable: false
        timezone nullable: true
        photo nullable: true
        homepage url: true, nullable: true
        country nullable: true
        jabberAddress url: true, nullable:true
    }

}
