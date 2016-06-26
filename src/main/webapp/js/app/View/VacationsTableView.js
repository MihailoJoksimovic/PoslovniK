Poslovnik.VacationsTableView = Poslovnik.AbstractView.extend({
    events: {
        'click .edit-row' : 'onEditPaymentBtnClick',
        'click .save-row' : 'onSaveRowBtnClick',
        'click .cancel-edit-row' : 'onClickCancelEditRowBtnClick',
        'click .delete-row' : 'onDeleteRowBtnClick',
        'click #add-new-payment' : 'onAddNewRowBtnClick'
    },
    
    person: false,
    
    initialize: function(options) {
        if (options.person) {
            this.person = options.person;
        }
        
        this.render();
        
        this.listenTo(this.collection, 'fetch update reset', this.subRender);
    },
    
    render: function() {
        if (Poslovnik.Person.hasModeratorOrAdminPrivileges()) {
            this.$el.find('.show-in-employee-view').addClass('hidden');
        } else {
            this.$el.find('.show-in-admin-view').addClass('hidden');
        }
        
        this.subRender();
    },
    
    subRender: function() {
       if (this.collection.length === 0) {
           return;
       }
       
       var self = this;
       
       var tbody = this.$el.find('tbody');
       
       var userIsModeratorOrAdmin = Poslovnik.Person.hasModeratorOrAdminPrivileges();
       
       var template = '<tr data-cid="<%= cid %>"><td><%= date_from %></td><td><%= date_to %></td><td><%= status %></td>';
       
       if (userIsModeratorOrAdmin) {
           template += '<td>';
           template += '<a href="javascript: void(0);" data-cid="<%= cid %>" class="edit-row glyphicon glyphicon-pencil"></a>';
           template += "&nbsp;<a href='javascript: void(0);' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove'></a>";
           template += '</td>';
       } else {
            template += '<td>';
            template += "<a href='javascript: void(0);' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove <%= hide_remove_vacation %>'></a>";
            template += '</td>';
            
       }
       
       template += '</tr>';
       
       template = _.template(template);
       
       var html = "";
       
       this.collection.each(function(model) {
           if (model.get('new') === true) {
               var rowHtml = self.getNewRowHtml(model);
           } else {
               var hideRemoveVacation = "";
               if (!Poslovnik.Person.hasModeratorOrAdminPrivileges() && model.get('status') != 'pending') {
                   hideRemoveVacation = "hidden";
               }
               
               var rowHtml = template({
                    cid: model.cid,
                    date_from: Poslovnik.Person.formatDate(model.get('date_from')),
                    date_to: Poslovnik.Person.formatDate(model.get('date_to')),
                    status: model.get('status'),
                    hide_remove_vacation: hideRemoveVacation
                });
           }
          
          html += rowHtml;
       });
       
       $(tbody).html(html);
       
       this.$el.find('[name=date_from]').datepicker({
            altField: this.$el.find('[name=alt_date_from]')
        });
        
        this.$el.find('[name=date_to]').datepicker({
            altField: this.$el.find('[name=alt_date_to]')
        });
    },
    
    onEditPaymentBtnClick: function() {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        var row = this.$el.find('tr[data-cid='+cid+']');
        
        var rowHtml = this.getEditRowHtml(model);
        
        rowHtml += "<td><a href='javascript: void(0);' data-cid='"+cid+"' class='save-row glyphicon glyphicon-floppy-disk'></a>&nbsp;<a href='javascript: void(0);' style='' data-cid='"+cid+"' class='cancel-edit-row glyphicon glyphicon-remove'></a></td>";

        $(row).html(rowHtml);
        
        this.$el.find('[name=date_from]').datepicker({
            altField: this.$el.find('[name=alt_date_from]')
        });
        
        this.$el.find('[name=date_to]').datepicker({
            altField: this.$el.find('[name=alt_date_to]')
        });
    },
    
    onClickCancelEditRowBtnClick: function() {
        this.collection.fetch();
    },
    
    onDeleteRowBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        var row = this.$el.find('tr[data-cid='+cid+']');
        
        var confirmation = confirm("Are you sure?");
        
        if (!confirmation) {
            return;
        }
        
        model.destroy( { url: "vacation?action=delete&id=" + model.get('id') } );
        
        this.collection.remove(model);
            
        $(row).remove();

    },
    
    onAddNewRowBtnClick: function() {
        var model = new Backbone.Model();
        
        model.set('person_id', this.person.get('id'));
        model.set('new', true);
        
        this.collection.add(model, { at: 0 });
    },
    
    onSaveRowBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        this.copyDatafromFormToModel(model, cid);
        
        if (model.get('new')) {
            var url = 'vacation?action=add&person_id='+this.person.get('id');
        } else {
            var url = 'vacation?action=edit&id='+model.get('id');
        }
        
        var errorFn = function() {
            // Display unknown error modal
            
            $('#unknown-error-modal').modal();
        };
        
        var self = this;
        
        var successFn = function() {
            self.showSuccess("Vacation request has been submitted successfully.");
            self.collection.fetch();
        };

        var errorFn = function(data, response) {
            switch (response.status) {
                case 400:
                    self.showError("Some fields are not filled! Please make sure to fill in all fields.");
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
    
    getNewRowHtml: function(model) {
        var id = model.get('id');
        
        var row = "<tr data-isnew='1' data-cid='<%= cid %>'>";
        row += this.getEditRowHtml(model);
        row += "<td>";
        row += "<a href='javascript: void(0);' style='' data-cid='<%= cid %>' class='save-row glyphicon glyphicon-floppy-disk'></a>&nbsp;";
        row += "<a href='javascript: void(0);' style='' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove'></a>";
        row += "</td>";
        row += "</tr>";
        
        var template = _.template(row);
        
        var rowHtml = template({
            cid: model.cid,
        });
      
        return rowHtml;
    },
    
    getEditRowHtml: function(model) {
        var row = '';
        row +='<td><input required type="text" name="date_from" placeholder="From ..." class="form-control input-lg" value="<%= date_from %>" /><input type="hidden" name="alt_date_from" value="<%= date_from_raw %>" /></td>';
        row +='<td><input required type="text" name="date_to" placeholder="To ..." class="form-control input-lg" value="<%= date_to %>" /><input type="hidden" name="alt_date_to" value="<%= date_to_raw %>" /></td>';
        
        if (Poslovnik.Person.hasModeratorOrAdminPrivileges()) {
            // Allow editing only if user is Moderator or Admin
            
            var vacationStatuses = ['pending', 'approved', 'rejected'];
        
            var rowVacationStatuses = '<td><select name="status" class="form-control input-lg">';

            _.each(vacationStatuses, function(val) {
               var selected = "";
               if (val == model.get('status')) {
                   selected = "selected";
               }

               rowVacationStatuses += '<option value="'+val+'" '+selected+'>'+val+'</option>'; 
            });

            rowVacationStatuses += '</select></td>';
        } else {
            var rowVacationStatuses = "<td></td>";
        }
        
        
        
        row += rowVacationStatuses;
        
        row = _.template(row);
                
        return row({
            date_from: Poslovnik.Person.formatDate(model.get('date_from')),
            date_to: Poslovnik.Person.formatDate(model.get('date_to')),
            date_from_raw: model.get('date_from'),
            date_to_raw: model.get('date_to')
        });
    },
    
    copyDatafromFormToModel: function(model, cid) {
        var self = this;
        
        var tr = $('tr[data-cid='+cid+']');
        
        var date_from = $(tr).find('input[name=alt_date_from]').val(); // alt_date holds date in yyyy-mm-dd format
        var date_to = $(tr).find('input[name=alt_date_to]').val(); // alt_date holds date in yyyy-mm-dd format
        var status = $(tr).find('select[name=status]').val();
        
        var allFields = [date_from, date_to, status];
        
        _.each(allFields, function(field) {
            if (field.length == 0) {
               self.showError("All fields are mandatory!");
               
               throw "Validation failed!";
            }
        });
        
        
        model.set('date_from', date_from);
        model.set('date_to', date_to);
        model.set('status', status);
    }
    
});