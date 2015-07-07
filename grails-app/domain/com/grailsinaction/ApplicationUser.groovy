package com.grailsinaction

class ApplicationUser {

    String applicationName
    String password
    String apiKey

    static constraints = {
        importFrom User, include: ['password']
        //OR 'importFrom User' OR regexp OR exclude

        applicationName blank: false, unique: true
        apiKey blank: false
    }
}
