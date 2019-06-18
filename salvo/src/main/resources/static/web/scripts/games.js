var app = new Vue({
            el: '#game',
            data: {
                games: {},
                player: {},
                stats: {},
                table: '<tr> <th>Name</th> <th>Total</th> <th>Won</th> <th>Lost</th> <th>Tied</th></tr>',
                list:  [],
                score: 0,
                won: 0,
                lost: 0,
                tied: 0,
                leaderboard: {}
            }});

            fetch('/api/games').then(function (response) {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.statusText);
            }).then(function (value) {
                app.games = value;
                app.player = value.player;
                app.stats = value.stats;
                app.leaderboard;
                console.log(value);
            }).catch(function (error) {
                console.log("Request failed: " + error.message);
            });

            function login(evt) {
                             evt.preventDefault(evt);
                             var form = evt.target.form;
                             $.post("/api/login",
                                    {
                                                                                       email: form["email"].value,
                                                                                       password: form["password"].value  });
                           }

            function logout(evt) {
                             evt.preventDefault();
                             $.post("/api/logout");
                           }

            function signin(evt) {
           evt.preventDefault(evt);
           var form = evt.target.form;
                                        $.post("/api/players",
                                               {
                                                 email: form["email"].value,
                                                 password: form["password"].value });
                                                 }
