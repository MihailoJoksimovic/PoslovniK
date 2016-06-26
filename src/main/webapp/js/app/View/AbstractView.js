Poslovnik.AbstractView = Backbone.View.extend({
   showSuccess: function(text) {
        var self = this;
        
        this.hideAllAlerts();
        
        this.$el.find('#alert-divs').show();
        
        this.$el.find('.alert-success').html(text).show();
        
        var timeoutVar = 'timeoutSuccess'+ this.cid;
        
        window.clearTimeout(window[timeoutVar]);
        
        window[timeoutVar] = window.setTimeout(function() {
            self.hideAllAlerts(500);
        }, 4000);
    },
    
    showError: function(text) {
        this.hideAllAlerts();
        
        this.$el.find('#alert-divs').show(100);
        
        this.$el.find('.alert-danger').html(text).show();
    },
    
    hideAllAlerts: function(delay) {
        this.$el.find('#alert-divs').hide(delay);
        
        this.$el.find('#alert-divs .alert').hide();
    } 
});