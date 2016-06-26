Poslovnik.VacationCollection = Backbone.Collection.extend({
    person: null,
    
    initialize: function(data, options) {
        if (!options.person) {
            throw "Missing 'person' argument"
        }
        
        if (!options.comparator) {
            this.comparator = 'date_from';
        }
        
        this.person = options.person;
    },
    
    url: function () {
        var url = 'vacation?action=list&id='+this.person.get('id');
        
        return url;
    }
});