Poslovnik.AdminDashboard = Backbone.View.extend({
    initialize: function() {
        this.render();
    },
    
    render: function() {
        var tpl = _.template($('#tpl-admin-dashboard').html());
        
        this.$el.html(tpl);

        return this;
    }
});
