Poslovnik.EmployeeDashboard = Backbone.View.extend({
    
    initialize: function() {
        this.render();
        
        var collection = new Poslovnik.PayoutCollection();
        
        collection.add({amount: 100, date: "2016-01-01", type: "salary", description: "January Salary"} );
        collection.add({amount: 200, date: "2016-02-01", type: "salary", description: "February Salary"} );
        collection.add({amount: 300, date: "2016-03-01", type: "salary", description: "March Salary"} );
        
        var payoutTableView = new Poslovnik.PayoutTableView({
            el: this.$el.find('#payroll-list'),
            collection: collection
        });

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
