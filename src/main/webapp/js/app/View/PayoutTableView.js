Poslovnik.PayoutTableView = Poslovnik.AbstractView.extend({
    events: {
        'click .edit-payment' : 'onEditPaymentBtnClick',
        'click .save-row' : 'onSavePaymentBtnClick',
        'click .cancel-edit-row' : 'onClickCancelEditRowBtnClick',
        'click .delete-row' : 'onDeleteRowBtnClick',
        'click #add-new-payment' : 'onAddNewPaymentBtnClick'
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
        if (!Poslovnik.Person.hasModeratorOrAdminPrivileges()) {
            this.$el.find('.show-in-admin-view').addClass('hidden');
        }
        
        this.subRender();
    },
    
    subRender: function() {
       var self = this;
       
       var tbody = this.$el.find('tbody');
        
       if (this.collection.length === 0) {
           $(tbody).html("<tr><td colspan='10'>No data</td></tr>");
           
           return;
       }
       
       var userIsModeratorOrAdmin = Poslovnik.Person.hasModeratorOrAdminPrivileges();
       
       var template = '<tr data-cid="<%= cid %>"><td><%= amount %></td><td><%= date %></td><td><%= type %></td><td><%= description %></td>';
       
       if (userIsModeratorOrAdmin) {
           template += '<td>';
           template += '<a href="javascript: void(0);" data-cid="<%= cid %>" class="edit-payment glyphicon glyphicon-pencil"></a>';
           template += "&nbsp;<a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove'></a>";
           template += '</td>';
       }
       
       template += '</tr>';
       
       template = _.template(template);
       
       var html = "";
       
       this.collection.each(function(model) {
           if (model.get('new') === true) {
               var rowHtml = self.getNewRowHtml(model);
           } else {
               var rowHtml = template({
                    cid: model.cid,
                    amount: model.get('amount'),
                    date: Poslovnik.Person.formatDate(model.get('date')),
                    description: model.get('description'),
                    type: model.get('type')
                });
           }
          
          html += rowHtml;
       });
       
       $(tbody).html(html);
       
       this.$el.find('[name=date]').datepicker({
            altField: this.$el.find('[name=alt_date]')
        });
    },
    
    onEditPaymentBtnClick: function() {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        var row = this.$el.find('tr[data-cid='+cid+']');
        
        var rowHtml = this.getEditRowHtml(model);
        
        rowHtml += "<td><a href='javascript: void(0);' style='font-size: 16px' data-cid='"+cid+"' class='save-row glyphicon glyphicon-floppy-disk'></a>&nbsp;<a href='javascript: void(0);' style='font-size: 16px' data-cid='"+cid+"' class='cancel-edit-row glyphicon glyphicon-remove'></a></td>";

        $(row).html(rowHtml);
        
        this.$el.find('[name=date]').datepicker({
            altField: this.$el.find('[name=alt_date]')
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
        
        model.destroy( { url: "payout?action=delete&id=" + model.get('id') } );
        
        this.collection.remove(model);
            
        $(row).remove();
        
        this.showSuccess("Entry has been removed successfully!");

    },
    
    onAddNewPaymentBtnClick: function() {
        var model = new Backbone.Model();
        
        model.set('person_id', this.person.get('id'));
        model.set('new', true);
        
        this.collection.add(model, { at: 0 });
    },
    
    onSavePaymentBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        this.copyDatafromFormToModel(model, cid);
        
        if (model.get('new')) {
            var url = 'payout?action=add&person_id='+this.person.get('id');
        } else {
            var url = 'payout?action=edit&id='+model.get('id');
        }
        
        var errorFn = function() {
            // Display unknown error modal
            
            $('#unknown-error-modal').modal();
        };
        
        var self = this;
        
        var successFn = function() {
            self.showSuccess("Payment has been saved successfully!");
            self.collection.reset();
            self.collection.fetch();
        };

        var errorFn = function(data, response) {
            switch (response.status) {
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
    
    getNewRowHtml: function(model) {
        var id = model.get('id');
        
        var row = "<tr data-isnew='1' data-cid='<%= cid %>'>";
        row += this.getEditRowHtml(model);
        row += "<td>";
        row += "<a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='save-row glyphicon glyphicon-floppy-disk'></a>&nbsp;";
        row += "<a href='javascript: void(0);' style='font-size: 16px' data-cid='<%= cid %>' class='delete-row glyphicon glyphicon-remove'></a>";
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
        row +='<td><input required type="number" name="amount" placeholder="Enter payment amount here ..." value="<%= amount %>" class="form-control" /></td>';
        row +='<td><input required type="text" name="date" placeholder="Enter payment date here ..." value="<%= formatted_date %>" class="form-control" /><input type="hidden" name="alt_date" value="<%= date_raw %>" /></td>';
        
        var paymentTypes = ['salary', 'bonus', 'other'];
        
        var rowPaymentTypes = '<td><select name="type" class="form-control">';
        
        _.each(paymentTypes, function(val) {
           rowPaymentTypes += '<option value="'+val+'">'+val+'</option>'; 
        });
        
        rowPaymentTypes += '</select></td>';
        
        row += rowPaymentTypes;
        
        row +='<td><input required type="text" name="description" placeholder="Enter payment description here (e.g. Salary for June)" value="<%= description %>" class="form-control" /></td>';
        
        row = _.template(row);
        
        return row({
            amount: model.get('amount'),
            formatted_date: model.get('date') ? Poslovnik.Person.formatDate(model.get('date')) : '',
            date_raw: model.get('date'),
            description: model.get('description')
        });
    },
    
    copyDatafromFormToModel: function(model, cid) {
        var self = this;
        
        var tr = $('tr[data-cid='+cid+']');
        
        var amount = $(tr).find('input[name=amount]').val();
        var date = $(tr).find('input[name=alt_date]').val(); // alt_date holds date in yyyy-mm-dd format
        var type = $(tr).find('select[name=type]').val();
        var description = $(tr).find('input[name=description]').val();
        
        var allFields = [amount, date, type, description];
        
        _.each(allFields, function(field) {
           if (field.length == 0) {
               self.showError("All fields are mandatory!");
               
               throw "Validation failed!";
           } 
        });
        
        
        model.set('amount', amount);
        model.set('date', date);
        model.set('type', type);
        model.set('description', description);
    }
}, Poslovnik.AlertsView);

