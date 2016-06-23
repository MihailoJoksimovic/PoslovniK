Poslovnik.EmployeeDashboard = Backbone.View.extend({
    
    initialize: function() {
        this.render();
        
        var person = Poslovnik.Person;
        
        var collection = new Poslovnik.PayoutCollection({
            person: person
        });

        var payoutTableView = new Poslovnik.PayoutTableView({
            el: this.$el.find('#payroll-list'),
            collection: collection
        });
        
        collection.fetch();
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
