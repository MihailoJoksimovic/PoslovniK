Poslovnik.AdminDashboard = Backbone.View.extend({
    personCollection: new Poslovnik.PersonCollection(),
    
    events: {
        'click #add-new-btn' : 'onAddNewBtnClick',
        'click .delete-row' : 'onDeleteRowBtnClick',
        'click .save-row' : 'onSaveRowBtnClick'
    },
    
    initialize: function() {
        this.render();
        
        personCollection = this.personCollection;
        
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
        
        $(tbody).html("");
        
        var self = this;
        
        this.personCollection.each(function(person) {
            if (person.get('new') === true) {
                var row = self.getNewPersonRowHtml(person);
                
                tbody.prepend(row);
                
            } else {
                var row = self.getExistingPersonRowHtml(person);
                
                tbody.append(row);
            }
            
           
           
        });
    },
    
    getNewPersonRowHtml: function(person) {
        var id = person.get('id');
        
        var row = "<tr data-isnew='1' data-cid='<%= cid %>'>";
        row+="<td style='text-align: center;'></td>";
        row+='<td><input type="email" name="email" /></td>';
        row+='<td><input type="password" name="password" /></td>';
        row+='<td><select name="title"><option value="mr">mr</option><option value="ms">ms</option></select></td>';
        row+="<td><input type='text' name='first_name' /></td>";
        row+="<td><input type='text' name='last_name' /></td>";
        row+="<td><%= position_html %></td>";
        row+="<td><%= account_type_html %></td>";
        row += "<td>";
        row += "<a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='save-row glyphicon glyphicon-floppy-disk'></a>&nbsp;";
        row += "<a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove'></a>";
        row += "</td>";
        row += "</tr>";
        
        var template = _.template(row);
        
        var positionHtml = '<select name="position">';
        
        Poslovnik.Positions.each(function(position) {
            positionHtml += '<option value="'+position.get('id')+'">'+position.get('name')+'</option>';
        });
        positionHtml += "</select>";
        
        var accountTypeHtml = '<select name="account_type">';
        accountTypeHtml += '<option value="'+Poslovnik.PermissionLevels.ADMINISTRATOR+'">Administrator</option>';
        accountTypeHtml += '<option value="'+Poslovnik.PermissionLevels.MODERATOR+'">Moderator</option>';
        accountTypeHtml += '<option value="'+Poslovnik.PermissionLevels.REGULAR_USER+'">Employee</option>';
        accountTypeHtml += "</select>";
        
        var rowHtml = template({
            cid: person.cid,
            position_html: positionHtml,
            account_type_html: accountTypeHtml
        });
      
        return rowHtml;
    },
    
    getExistingPersonRowHtml: function(person) {
        var template = "<tr data-cid='<%= cid %>'><td style='text-align: center;'><input name='selected_person' data-id='<%= id %>' type='radio' /></td><td><%= email %></td><td><em>(hidden)</em><td><%= title %></td><td><%= first_name %></td><td><%= last_name %></td><td><%= position %></td><td><%= account_type %></td>";
           template += "<td><a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove' data-cid='<%= cid %>'></a></td>";
           template += "</tr>";
            
           var template = _.template(template); 
           
           var accountType = "Employee";
           
           switch(person.get('permission_level')) {
               case Poslovnik.PermissionLevels.ADMINISTRATOR:
                   accountType = "Administrator";
                   break;
               case Poslovnik.PermissionLevels.MODERATOR:
                   accountType = "Moderator";
                   break;
               case Poslovnik.PermissionLevels.REGULAR_USER:
                   accountType = "Employee";
                   break;
               default:
                   accountType = "";
                   break;
           }
           
           if (person.get('account_type') == Poslovnik.PermissionLevels.MODERATOR) {
               accountType = "Moderator";
           } else if (person.get('account_type') == Poslovnik.PermissionLevels.ADMINISTRATOR) {
               accountType = "Administrator";
           }
           
           var row = template({
               id: person.get('id'),
               cid: person.cid,
               email: person.get('email'),
               title: person.get('title'),
               first_name: person.get('first_name'),
               last_name: person.get('last_name'),
               position: person.get('position_name'),
               account_type: accountType
           });
           
           
           return row;
    },
    
    onAddNewBtnClick: function() {
        var person = new Poslovnik.PersonModel();
        person.set('new', true);
        
        this.personCollection.add(person);
    },
    
    onSaveRowBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.personCollection.get({ cid: cid });
        
        this.copyDatafromFormToModel(model, cid);
        
        var url = 'person?action=add';
        
        var errorFn = function() {
            // Display unknown error modal
            
            $('#unknown-error-modal').modal();
        };
        
        var self = this;
        
        var successFn = function() {
            self.showSuccess("Person has been added!");
            self.personCollection.fetch();
        };

        var errorFn = function(data, response) {
            if (response.status == 409) {
                self.showError("User with that email address already exists!");

                return;
            }
            
            switch (response.status) {
                case 409:
                    self.showError("User with that email address already exists!");
                    break;
                case 400:
                    self.showError("Some fields are not field! Please make sure to fill in all fields.");
                    break;
                default:
                    self.showError("An unknown error has occurred ("+response.status+").");
                    break;
            }
            
            
        };
        
        model.save(
            {}, 
            {
                url: url, 
                success: successFn,
                error: errorFn
            },
            self
        );
    },
    
    
    
    onDeleteRowBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.personCollection.get({ cid: cid });
         
        model.destroy( { url: "person?action=delete&id=" + model.get('id') } );        
        
    },
    
    copyDatafromFormToModel: function(model, cid) {
        var tr = $('tr[data-cid='+cid+']');
        
        var email = $(tr).find('input[name=email]').val();
        var password = $(tr).find('input[name=password]').val();
        var first_name = $(tr).find('input[name=first_name]').val();
        var last_name = $(tr).find('input[name=last_name]').val();
        var title = $(tr).find('select[name=title]').val();
        var position = $(tr).find('select[name=position]').val();
        var accountType = $(tr).find('select[name=account_type]').val();
        
        model.set('email', email);
        model.set('password', password);
        model.set('title', title);
        model.set('first_name', first_name);
        model.set('last_name', last_name);
        model.set('position', position);
        model.set('account_type', accountType);
    },
    
    showSuccess: function(text) {
        this.hideAllAlerts();
        this.$el.find('.alert-success').html(text).removeClass('hidden');
    },
    
    showError: function(text) {
        this.hideAllAlerts();
        this.$el.find('.alert-danger').html(text).removeClass('hidden');
    },
    
    hideAllAlerts: function() {
        this.$el.find('.alert').addClass('hidden');
    }
});
