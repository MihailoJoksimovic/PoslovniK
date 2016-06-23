Poslovnik.ManageEmployee = Backbone.View.extend({
   person: null,
    
   initialize: function(options) {
       if (!options.person) {
           throw "Missing person object!";
       }
       
       this.person = options.person;
       
       this.render();
   },
   
   render: function() {
       if ( ! this.person.get('first_name')) {
           // TODO: Initialize the person and delay rendering ...
           
           return;
       }
       
       this.subRender();

       return this;
   },
   
   subRender: function() {
       var template = _.template($('#tpl-manage-employee').html());
       
       var html = template();
       
       this.$el.html(html);
   }
});