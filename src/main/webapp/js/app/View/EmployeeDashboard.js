Poslovnik.EmployeeDashboard = Backbone.View.extend({
    
    initialize: function() {
        this.render();

    },

    render: function() {
        var tpl = _.template($('#tpl-employee-dashboard').html());
        
        this.$el.html(tpl);

        return this;
    },
    
    subRender: function() {
        
    }
});
