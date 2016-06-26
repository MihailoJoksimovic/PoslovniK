<%@page import="java.util.List"%>
<%@page import="com.poslovnik.model.data.Position"%>
<%@page import="com.poslovnik.model.dao.EntityManagerWrapper"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="com.poslovnik.model.dao.PositionDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script   src="https://code.jquery.com/jquery-2.2.4.min.js"   integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="   crossorigin="anonymous"></script>
        <script type="text/javascript" src="js/underscore.min.js"></script>
        <script type="text/javascript" src="js/backbone.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script type="text/javascript" src="/scripts/jquery.ui.datepicker-it.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-default" id="top-navbar">
            <div class="container-fluid">
              <!-- Brand and toggle get grouped for better mobile display -->
              <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">PoslovniK</a>
              </div>

              <ul class="nav navbar-nav navbar-right">
                <li><a href="javascript: void(0)" id="log-out-link">Log out</a></li>
              </ul>
            </div><!-- /.container-fluid -->
      </nav>
    
    <div id="content" class="container-fluid">
        
    </div>
    
    <%@include  file="pages/modals.html" %>
    
    <script type="text/template" id="tpl-login">
        <%@include  file="pages/login.html" %>
    </script>
    
    <script type="text/template" id="tpl-admin-dashboard">
        <%@include  file="pages/admin_dashboard.html" %>
    </script>
    
    <script type="text/template" id="tpl-employee-dashboard">
        <%@include  file="pages/employee_dashboard.html" %>
    </script>
    
    <script type="text/template" id="tpl-manage-employee">
        <%@include  file="pages/manage_employee.html" %>
    </script>
    
    <script>Poslovnik = {};</script>
    
    <script>
        Poslovnik = Poslovnik || {};
        
        // Prefill list of positions so that we can use it later
        // when creating a collection 
        
        Poslovnik.PositionsList = [];
        
        <%
            EntityManager em = EntityManagerWrapper.getEntityManager();
            List<Position> positions = PositionDAO.getInstance().findAll(em);
            
            for (Position p : positions) {
        %>
            Poslovnik.PositionsList.push({
                id: <%= p.getId() %>,
                name: "<%= p.getName() %>"
            });
        <%
            }
        %>
            
            Poslovnik.PermissionLevels = {
                REGULAR_USER: 10,
                MODERATOR: 50,
                ADMINISTRATOR: 100
            };
    </script>
    
    <script type="text/javascript" src="js/moment.js"></script>
    <script type="text/javascript" src="js/app/Model/Person.js"></script>
    <script type="text/javascript" src="js/app/Collection/PersonCollection.js"></script>
    <script type="text/javascript" src="js/app/Collection/PositionCollection.js"></script>
    <script type="text/javascript" src="js/app/Collection/PayoutCollection.js"></script>
    <script type="text/javascript" src="js/app/Collection/VacationCollection.js"></script>
    <script type="text/javascript" src="js/app/View/Home.js"></script>
    <script type="text/javascript" src="js/app/View/Login.js"></script>
    <script type="text/javascript" src="js/app/View/AdminDashboard.js"></script>
    <script type="text/javascript" src="js/app/View/EmployeeDashboard.js"></script>
    <script type="text/javascript" src="js/app/View/ManageEmployee.js"></script>
    <script type="text/javascript" src="js/app/View/PayoutTableView.js"></script>
    <script type="text/javascript" src="js/app/View/VacationsTableView.js"></script>
    <script type="text/javascript" src="js/app/View/Navbar.js"></script>
    <script type="text/javascript" src="js/app/router.js"></script>
    <script type="text/javascript" src="js/app/bootstrap.js"></script>
    </body>
</html>
