/**
 * Bootstraps PoslovniK web app :)
 */

// Backend supports only GET & POST so ... yeah :)
Backbone.emulateHTTP = true;

Poslovnik.Person = new Poslovnik.PersonModel();

Poslovnik.Router = new Router();

Poslovnik.Positions = new Poslovnik.PositionCollection(Poslovnik.PositionsList);

Backbone.history.start();

// Do this only once when starting the app -- check if user is already
// logged in, and if he is - redirect him to the dashboard page

Backbone.Events.listenToOnce(Poslovnik.Person, 'login_status_changed', function() {
    if (Poslovnik.Person.isLoggedIn()) {
        Poslovnik.Router.navigate('dashboard', { trigger: true });
    }
});

Poslovnik.Person.checkIfLoggedIn();