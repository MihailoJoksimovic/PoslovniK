Poslovnik.ManageEmployee = Backbone.View.extend({
   person: null,
    
   initialize: function(options) {
       if (!options.person) {
           throw "Missing person object!";
       }
       
       this.person = options.person;
       
       this.render();
        
        var collection = new Poslovnik.PayoutCollection([], {
            person: this.person
        });

        var payoutTableView = new Poslovnik.PayoutTableView({
            el: this.$el.find('#payroll-list'),
            collection: collection,
            person: this.person
        });
        
        collection.fetch();

   },
   
   render: function() {
       if ( ! this.person.get('first_name')) {
           // TODO: Initialize the person and delay rendering ...
           
           return;
       }
       
       var template = _.template($('#tpl-manage-employee').html());
       
       var html = template();
       
       this.$el.html(html);
       
       this.subRender();

       return this;
   },
   
   subRender: function() {
       
   }
});