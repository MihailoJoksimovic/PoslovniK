Poslovnik.PayoutCollection = Backbone.Collection.extend({
    person: null,
    
    initialize: function(options) {
        if (!options.person) {
            throw "Missing 'person' argument"
        }
        
        this.person = options.person;
    },
    
    url: function () {
        var url = 'payout?action=list&id='+this.person.get('id');
        
        return url;
    }
});