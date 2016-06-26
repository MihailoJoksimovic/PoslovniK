Poslovnik.ManageEmployee = Backbone.View.extend({
   person: null,
    
   initialize: function(options) {
       if (!options.person) {
           throw "Missing person object!";
       }
       
       this.person = options.person;
       
       this.render();
        
        var payoutsCollection = new Poslovnik.PayoutCollection([], {
            person: this.person
        });

        var payoutTableView = new Poslovnik.PayoutTableView({
            el: this.$el.find('#payroll-list'),
            collection: payoutsCollection,
            person: this.person
        });
        
        payoutsCollection.fetch();
        
        var vacationsCollection = new Poslovnik.VacationCollection([], {
            person: this.person
        });

        var vacationTableView = new Poslovnik.VacationsTableView({
            el: this.$el.find('#vacations-list'),
            collection: vacationsCollection,
            person: this.person
        });
        
        vacationsCollection.fetch();
        
        // Add some random data
        
//        vacationsCollection.add({ date_from: '2016-01-01', date_to: '2016-01-31', status: 'approved'});
//        vacationsCollection.add({ date_from: '2016-02-01', date_to: '2016-02-28', status: 'rejected'});
//        vacationsCollection.add({ date_from: '2016-03-01', date_to: '2016-03-31', status: 'pending'});

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