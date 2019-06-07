var app = new Vue({
            el: '#game',
            data: {
                games: {},
                table: '<tr> <th>Name</th> <th>Total</th> <th>Won</th> <th>Lost</th> <th>Tied</th></tr>',
                list:  [],
                score: 0,
                won: 0,
                lost: 0,
                tied: 0,
                leaderboard: {}
            },
            methods: {
                            leaderboard: function () {
                                for(var i = 0; i < games.stats.length; i++){
                                if(!(list.includes(games.stats[i].email))){

                                score = games.stats[i].score;
                                won = games.stats[i].won;
                                tied = games.stats[i].tied;
                                lost = games.stats[i].lost;

                                table += '<tr><td>'+games.stats[i].email+
                                '</td><td>'+score+'</td><td>'+won+'</td><td>'+lost+'</td><td>'+tied+'</td></tr>';
                                list += games.stats[i].email;
                            }
                    }
                            },
                            }});

            fetch('/api/games').then(function (response) {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.statusText);
            }).then(function (value) {
                app.games = value;
                app.leaderboard;
                console.log(app.games);
            }).catch(function (error) {
                console.log("Request failed: " + error.message);
            });