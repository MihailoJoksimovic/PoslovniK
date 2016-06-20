Poslovnik.EmployeeDashboard = Backbone.View.extend({
    
    initialize: function() {
        this.render();

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
