var Router = Backbone.Router.extend({

  routes: {
    "": "home",
    "login": "home",
    "dashboard" : "dashboard"
  },
  
  currentView: new Backbone.View(),
  
  home: function() {
      // For starters, just display the log in page
      var view = new Poslovnik.Login({
          el: $('#content')
      });
      
      this.currentView = view;
  },
  
  dashboard: function() {
      var person = Poslovnik.Person;
      
      if (!person.isLoggedIn()) {
          Poslovnik.Router.navigate('login', {trigger: true});
          
          return;
      }
      
      this.currentView.remove();
      
      console.log("Welcome to dashboard");
  }


});

