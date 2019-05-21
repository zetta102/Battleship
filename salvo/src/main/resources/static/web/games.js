var app = new Vue({
            el: '#game',
            data: {
                games: {},
            }});

            fetch('/api/games').then(function (response) {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.statusText);
            }).then(function (value) {
                app.games = value;
            }).catch(function (error) {
                console.log("Request failed: " + error.message);
            });