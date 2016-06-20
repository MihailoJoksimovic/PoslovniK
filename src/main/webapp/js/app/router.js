var Router = Backbone.Router.extend({

  routes: {
    "": "home",
    "login": "home",
    "dashboard" : "dashboard"
  },
  
  currentView: new Backbone.View(),
  
  initialize: function() {
      var navbarView = new Poslovnik.Navbar({
          el: $('#top-navbar')
      });
  },
  
  home: function() {
      this.currentView.remove();
      
      // For starters, just display the log in page
      var view = new Poslovnik.Login();
      
      view.$el.appendTo('#content');
      
      this.currentView = view;
  },
  
  dashboard: function() {
      var person = Poslovnik.Person;
      
      if (!person.isLoggedIn()) {
          Poslovnik.Router.navigate('login', {trigger: true});
          
          return;
      }
      
      this.currentView.remove();
      
      // TODO: Display view based on user's permission level
      
      var view = new Poslovnik.AdminDashboard();
      var view = new Poslovnik.EmployeeDashboard();
      
      view.$el.appendTo('#content');
      
      this.currentView = view;
  }


});

