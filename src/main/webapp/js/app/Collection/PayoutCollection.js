Poslovnik.PayoutCollection = Backbone.Collection.extend({
    person: null,
    
    initialize: function(data, options) {
        if (!options.person) {
            throw "Missing 'person' argument"
        }
        
        if (!options.comparator) {
            this.comparator = 'date';
        }
        
        this.person = options.person;
    },
    
    url: function () {
        var url = 'payout?action=list&id='+this.person.get('id');
        
        return url;
    }
});