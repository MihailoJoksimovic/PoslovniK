Poslovnik.Login = Backbone.View.extend({
    events: {
        'click #log-in-btn' : 'onLogInBtnClicked'
    },
    
    person: false,
    
    initialize: function() {
        this.render();
        
        this.person = Poslovnik.Person;
    },
    
    render: function() {
        var tpl = _.template($('#tpl-login').html());
        
        this.$el.html(tpl);
        
        return this;
    },
    
    onLogInBtnClicked: function() {
        var person = this.person;

        person.set('email', this.$el.find('#email').val());
        person.set('password', this.$el.find('#password').val());
        
        this.listenToOnce(this.person, 'login_status_changed', this.onLoginStatusChanged);
        
        person.logIn();
    },
    
    onLoginStatusChanged: function() {
        var person = this.person;
        
        this.$el.find('#form-group').removeClass('has-error');
        
        if (person.isLoggedIn()) {
            Poslovnik.Router.navigate('dashboard', {trigger: true});
            
            return;
        }
        
        this.$el.find('#form-group').addClass('has-error');
    }
   
});

