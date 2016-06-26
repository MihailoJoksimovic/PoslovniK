Poslovnik.EmployeeDashboard = Backbone.View.extend({
    
    initialize: function() {
        this.render();
        
        var person = Poslovnik.Person;
        
        var payoutCollection = new Poslovnik.PayoutCollection([], {
            person: person
        });

        var payoutTableView = new Poslovnik.PayoutTableView({
            el: this.$el.find('#payroll-list'),
            collection: payoutCollection,
            person: person
        });
        
        payoutCollection.fetch();
        
        var vacationCollection = new Poslovnik.VacationCollection([], {
            person: person
        });

        var vacationTableView = new Poslovnik.VacationsTableView({
            el: this.$el.find('#vacations-list'),
            collection: vacationCollection,
            person: person
        });
        
        vacationCollection.fetch();
    },

    render: function() {
        var tpl = _.template($('#tpl-employee-dashboard').html());
        
        this.$el.html(tpl);
        
        this.subRender();

        return this;
    },
    
    subRender: function() {
        this.$el.find('.user-first-name').html(Poslovnik.Person.get('first_name'));
    }
});
