Poslovnik.PositionCollection = Backbone.Collection.extend({

    url: 'position?action=list',
    
    parse: function(response) {
        return response.data;
    }
});