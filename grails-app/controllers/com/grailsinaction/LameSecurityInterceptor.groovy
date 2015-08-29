package com.grailsinaction


class LameSecurityInterceptor {

    LameSecurityInterceptor() {
        match( controller:'post', action: '(addPost|deletePost)')
    }

    boolean before() {
        if (params.impersonateId) {
            session.user = User.findByLoginId(params.impersonateId)
        }
        if(!session.user) {
            redirect (controller: 'login', action: 'form')
            return false
        }
        true
    }

    boolean after() {
        true
    }

    void afterView() {
        // no-op
    }

}
