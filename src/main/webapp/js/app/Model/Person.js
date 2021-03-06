Poslovnik.PersonModel = Backbone.Model.extend({
    
    defaults: {
        'logged_in' : false
    },
    
    isLoggedIn: function() {
        return this.get('loggedIn') === true;
    },
    
    hasModeratorOrAdminPrivileges: function() {
        return this.get('permission_level') >= Poslovnik.PermissionLevels.MODERATOR;
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
        var self = this;
        
        var callback = function(data, textStatus, jqXHR) {
            if (data && data.success === true) {
                self.clear();
                
                Poslovnik.Router.navigate('login', { trigger: true } );
                
                return;
            }
        };

        $.post(
                'logout'
        ).always(callback);
    },
    
    // date must be Date object
    formatDate: function(date) {
        if (!date) {
            return '';
        }
        var format = this.getDateFormat();
        return moment(date).format(format)
    },
    
    getDateFormat: function() {
        return 'DD.MM.YYYY';
    },
    
    getFullname: function() {
        return this.get("first_name") + ' ' + this.get('last_name');
    }
});

