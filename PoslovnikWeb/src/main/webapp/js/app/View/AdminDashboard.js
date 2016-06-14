Poslovnik.AdminDashboard = Backbone.View.extend({
    personCollection: new Poslovnik.PersonCollection(),
    
    initialize: function() {
        this.render();
        
        this.listenTo(this.personCollection, 'update', this.onCollectionUpdated);
    },
    
    render: function() {
        var tpl = _.template($('#tpl-admin-dashboard').html());
        
        this.$el.html(tpl);
        
        this.personCollection.fetch();

        return this;
    },
    
    subRender: function() {
        
    },
    
    onCollectionUpdated: function() {
        var tbl = this.$el.find('#persons-table-list');
        
        var tbody = $(tbl).find('tbody');
        
        this.personCollection.each(function(person) {
           var template = "<tr><td style='text-align: center;'><input name='selected_person' data-id='<%= id %>' type='radio' /></td><td>aaa<%= email %></td><td><%= title %></td><td><%= first_name %></td><td><%= last_name %></td><td><%= position %></td><td><%= account_type %></td>";
           template += "<td><i>Actions</i></td>";
           template += "</tr>";
            
           var template = _.template(template); 
           
           var accountType = "Employee";
           
           if (person.get('account_type') == Poslovnik.PermissionLevels.MODERATOR) {
               accountType = "Moderator";
           } else if (person.get('account_type') == Poslovnik.PermissionLevels.ADMINISTRATOR) {
               accountType = "Administrator";
           }
           
           var row = template({
               id: person.get('id'),
               email: person.get('email'),
               title: person.get('title'),
               first_name: person.get('first_name'),
               last_name: person.get('last_name'),
               position: person.get('position_name'),
               account_type: accountType
           });
           
           tbody.append(row);
        });
    }
});
