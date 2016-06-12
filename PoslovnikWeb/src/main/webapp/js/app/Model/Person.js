Poslovnik.PersonModel = Backbone.Model.extend({
    defaults: {
        'logged_in' : false
    },
  
    isLoggedIn: function() {
        return this.get('loggedIn') === true;
    },
    
    logIn: function() {
        var self = this;
                
        var callback = function(data, textStatus, jqXHR) {
            var status = jqXHR.status;
            
            if (status == 200 && data.success && data.success === true) {
                self.set('loggedIn', true);
                
                self.trigger('login_status_changed');
                
                return;
            }
            
            self.set('loggedIn', false);
                
            self.trigger('login_status_changed');
        };

        var success = function(){};
        
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
    }
});

