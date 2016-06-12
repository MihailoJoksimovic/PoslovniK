Poslovnik.Login = Backbone.View.extend({
    events: {
        'click #log-in-btn' : 'onLogInBtnClicked'
    },
    
    person: false,
    
    initialize: function() {
        this.render();
        
        this.person = Poslovnik.Person;
        
        this.listenTo(this.person, 'login_status_changed', this.onLoginStatusChanged);
    },
    
    render: function() {
        var tpl = _.template($('#tpl-login').html());
        
        this.$el.html(tpl);
    },
    
    onLogInBtnClicked: function() {
        var person = this.person;

        person.set('email', this.$el.find('#email').val());
        person.set('password', this.$el.find('#password').val());
        
        person.logIn();
    },
    
    onLoginStatusChanged: function() {
        var person = this.person;
        
        if (person.isLoggedIn()) {
            Poslovnik.Router.navigate('dashboard', {trigger: true});
        }
    }
   
});

