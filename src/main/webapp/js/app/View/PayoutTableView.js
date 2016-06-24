Poslovnik.PayoutTableView = Backbone.View.extend({
    initialize: function() {
        this.render();
        
        this.listenTo(this.collection, 'fetch update reset', this.subRender);
    },
    
    render: function() {
        this.subRender();
    },
    
    subRender: function() {
       var tbody = this.$el.find('tbody');
       
       var template = '<tr><td><%= amount %></td><td><%= date %></td><td><%= type %></td><td><%= description %></td></tr>';
       template = _.template(template);
       
       var html = "";
       
       this.collection.each(function(model) {
          var rowHtml = template({
              amount: model.get('amount'),
              date: model.get('date'),
              description: model.get('description'),
              type: model.get('type')
          });
          
          html += rowHtml;
       });
       
       console.log($(tbody));
       $(tbody).html(html);
    }
});