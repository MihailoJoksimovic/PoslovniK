Poslovnik.Navbar = Backbone.View.extend({
    events: {
        'click #log-out-link' : 'onLogoutlinkClick'
    },
    
    initialize: function() {
        this.render();
        
        this.listenTo(Poslovnik.Person, 'change:loggedIn', this.onLoggedInStatusChanged);
    },
    
    render: function() {
        this.subRender();
        return this;
    },
    
    subRender: function() {
        if ( ! Poslovnik.Person.isLoggedIn()) {
            this.$el.find('#log-out-link').addClass('hidden');
        } else {
            this.$el.find('#log-out-link').removeClass('hidden');
        }
    },
    
    onLoggedInStatusChanged: function() {
        this.subRender();
    },
    
    onLogoutlinkClick: function() {
        Poslovnik.Person.logOut();
    }
});

