package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {

    def "Saving our first user to the database"(){
        given: "A brand new user"
        def joe = new User(loginId: 'joe',password: 'secret'/*,
                homepage: 'http://www.grailsinaction.com'*/)

        when: "the user is saved"
        joe.save()

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount ==0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes it's properties"(){
        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret'/*,
                homepage: 'http://www.grailsinaction.com'*/)
        existingUser.save(failOnError: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)
        foundUser.password = 'sesame'
        foundUser.save(failOnError: true)

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == 'sesame'
    }

    def "Deleting an existing user removes it from the database"(){

        given: "An existing user"
        def user = new User(loginId: 'joe', password: 'secret'/*,
                homepage: 'http://www.grailsinaction.com'*/)
        user.save(failOnError: true)

        when: "The user is deleted"
        def foundUser = User.get(user.id)
        foundUser.delete(flush: true)

        then: "The user is removed from the database"
        !User.exists(foundUser.id)
    }

    def "Saving a user with invalid properties causes an error"(){
        given: "A user which fails several field validations"
        def user = new User(loginId: 'joe', password: 'tiny'/*,
                homepage: 'not-a-url'*/)

        when: "The user is validated"
        user.validate()

        then:
        user.hasErrors()

        "size.toosmall" == user.errors.getFieldError("password").code
        "tiny" == user.errors.getFieldError("password").rejectedValue
        /*"url.invalid" == user.errors.getFieldError("homepage").code
        "not-a-url" == user.errors.getFieldError("homepage").rejectedValue*/
        !user.errors.getFieldError("loginId")
    }

    def "Recovering from a failed save by fixig invalid properties"(){
        given: "A user that has invalid properties"
        def chuck = new User(loginId: 'chuck', password: 'tiny',
                /*homepage: 'not-a-url'*/)
        assert chuck.save() == null
        assert chuck.hasErrors()

        when: "We fix the invalid properties"
        chuck.password = "fistfist"
//        chuck.homepage = "http://www.chucknorrisfacts.com"
        chuck.validate()

        then: "The user saves and validates fine"
        !chuck.hasErrors()
        chuck.save()
    }

    def "Saving a user with invalid properties same passwd causes an error"(){
        given: "A user which fails several field validations"
        def user = new User(loginId: 'joeCor', password: 'joeCor',
                /*homepage: 'not-a-url'*/)

        when: "The user is validated"
        user.validate()

        then:
        user.hasErrors()

        "validator.invalid" == user.errors.getFieldError("password").code
        "joeCor" == user.errors.getFieldError("password").rejectedValue
        /*"url.invalid" == user.errors.getFieldError("homepage").code
        "not-a-url" == user.errors.getFieldError("homepage").rejectedValue*/
        !user.errors.getFieldError("loginId")
    }

    def "Ensure a user can follow other users"(){
        given: "a set of baseline users"
        def joe = new User(loginId: 'joe', password: 'password')
        def jane = new User(loginId: 'jane', password: 'password')
        def jill = new User(loginId: 'jill', password: 'password')

        when: "Joe follows Jane and Jill and Jill follows Jane"
        joe.addToFollowing(jane)
        joe.addToFollowing(jill)
        jill.addToFollowing(jane)

        then: "Follower counts should match following people"
        2 == joe.following.size()
        1 == jill.following.size()
    }

    def "Try and find out why bootstrap not working"(){
        when:
        def user = new User(loginId: "admin3", password: "secret")
        user.setProfile(new Profile(email: "admin@yourhost.com", fullName: "Administrator"))



        then:
        user.save(failOnError: true)
//        user.setProfile(profileex)


    }
}
