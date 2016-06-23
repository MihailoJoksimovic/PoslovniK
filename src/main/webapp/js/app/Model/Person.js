Poslovnik.PersonModel = Backbone.Model.extend({
    
    defaults: {
        'logged_in' : false
    },
    
    isLoggedIn: function() {
        return this.get('loggedIn') === true;
    },
    
    
    checkIfLoggedIn: function() {
        var self = this;
        
        var success = function(){};
        
        var callback = function(data, textStatus, jqXHR) {
            self.loginCallback(data, textStatus, jqXHR);
        };
        
        $.get(
            'login',
            {},
            success,
            'json'
        ).always(callback);
    },
    
    logIn: function() {
        var self = this;

        var success = function(){};
        
        var callback = function(data, textStatus, jqXHR) {
            self.loginCallback(data, textStatus, jqXHR);
        };
        
        // Make an ajax request and try to log in
        $.post(
            'login',
            {
                email: this.get('email'),
                password: this.get('password')
            },
            success,
            'json'
        ).always(callback);
    },
    
    loginCallback: function(data, textStatus, jqXHR) {
        var self = this;
        
        var status = jqXHR.status;
            
        if (status == 200 && data.success && data.success === true) {
            self.set('loggedIn', true);

            // userData object contains all the relevant user data (id, email, etc.)

            self.set(data.userData);

            self.trigger('login_status_changed');

            return;
        }

        self.set('loggedIn', false);

        self.trigger('login_status_changed');
    },
    
    logOut: function() {
        var callback = function(data, textStatus, jqXHR) {
            if (data && data.success === true) {
                Poslovnik.Router.navigate('login', { trigger: true } );
                
                return;
            }
        };

        $.post(
                'logout'
        ).always(callback);
    }
});

