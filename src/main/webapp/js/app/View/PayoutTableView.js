Poslovnik.PayoutTableView = Backbone.View.extend({
    events: {
        'click .edit-payment' : 'onEditPaymentBtnClick',
        'click .save-row' : 'onSavePaymentBtnClick',
        'click .cancel-edit-row' : 'onClickCancelEditRowBtnClick'
    },
    
    initialize: function() {
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
       var tbody = this.$el.find('tbody');
       
       var userIsModeratorOrAdmin = Poslovnik.Person.hasModeratorOrAdminPrivileges();
       
       var template = '<tr data-cid="<%= cid %>"><td><%= amount %></td><td><%= date %></td><td><%= type %></td><td><%= description %></td>';
       
       if (userIsModeratorOrAdmin) {
           template += '<td>';
           template += '<a href="javascript: void(0);" data-cid="<%= cid %>" class="edit-payment glyphicon glyphicon-pencil"></a>';
           template += '</td>';
       }
       
       template += '</tr>';
       
       template = _.template(template);
       
       var html = "";
       
       this.collection.each(function(model) {
          var rowHtml = template({
              cid: model.cid,
              amount: model.get('amount'),
              date: Poslovnik.Person.formatDate(model.get('date')),
              description: model.get('description'),
              type: model.get('type')
          });
          
          html += rowHtml;
       });
       
       $(tbody).html(html);
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
    
    onSavePaymentBtnClick: function(event) {
        var target = $(event.target);
        
        var cid = $(target).attr('data-cid');
        
        var model = this.collection.get({ cid: cid });
        
        this.copyDatafromFormToModel(model, cid);
        
        if (model.get('new')) {
            var url = 'payout?action=add';
        } else {
            var url = 'payout?action=edit&id='+model.get('id');
        }
        
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
    
    getEditRowHtml: function(model) {
        var row = '';
        row +='<td><input type="number" name="amount" value="<%= amount %>" /></td>';
        row +='<td><input type="text" name="date" value="<%= formatted_date %>" /><input type="hidden" name="alt_date" value="<%= date_raw %>" /></td>';
        
        var paymentTypes = ['salary', 'bonus', 'other'];
        
        var rowPaymentTypes = '<td><select name="type">';
        
        _.each(paymentTypes, function(val) {
           rowPaymentTypes += '<option value="'+val+'">'+val+'</option>'; 
        });
        
        rowPaymentTypes += '</select></td>';
        
        row += rowPaymentTypes;
        
        row +='<td><input type="text" name="description" value="<%= description %>" /></td>';
        
        row = _.template(row);
        
        return row({
            amount: model.get('amount'),
            formatted_date: Poslovnik.Person.formatDate(model.get('date')),
            date_raw: model.get('date'),
            description: model.get('description')
        });
    },
    
    copyDatafromFormToModel: function(model, cid) {
        var tr = $('tr[data-cid='+cid+']');
        
        var amount = $(tr).find('input[name=amount]').val();
        var date = $(tr).find('input[name=alt_date]').val(); // alt_date holds date in yyyy-mm-dd format
        var type = $(tr).find('select[name=type]').val();
        var description = $(tr).find('input[name=description]').val();
        
        
        model.set('amount', amount);
        model.set('date', date);
        model.set('type', type);
        model.set('description', description);
    }
    
    
});