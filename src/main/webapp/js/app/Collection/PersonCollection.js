Poslovnik.PersonCollection = Backbone.Collection.extend({
    model: Poslovnik.PersonModel,
    
    url: 'person?action=list',
    
    parse: function(response) {
        return response.data;
    }
});