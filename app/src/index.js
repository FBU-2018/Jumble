// Example express application adding the parse-server module to expose Parse
// compatible API routes.

var express = require('express');
var ParseServer = require('parse-server').ParseServer;
var path = require('path');

var databaseUri = process.env.DATABASE_URI || process.env.MONGODB_URI;

if (!databaseUri) {
  console.log('DATABASE_URI not specified, falling back to localhost.');
}

if (process.env.FCM_API_KEY) {
   pushConfig['android'] = { 
   apiKey: process.env.FCM_API_KEY || 'AAAAto1Azj4:APA91bEEDuiH6lHJyX4fsOn9RZfIW5f5UvizIl0NZeJV-m2y7fBdgUJiOPQC-FPsBwryzYvuo6RmpOx65X3q_KPUwPRR_N_E5HEa27enEms38Q08_AqtmBVYS366UGn5fg4iVrmQD0Zc9CxxwI5b-Z3zRut0mFn3lQ',
   senderId: process.env.SENDER_ID || '784053882430'};
}

//var server = new ParseServer({
//  databaseURI: 'mongodb://heroku_j8mkx7b1:2fgh2tjh27i9a88qimbdihsnvh@ds135441.mlab.com:35441/heroku_j8mkx7b1',
//  cloud: './cloud/main.js',
//  appId: 'fbu-2018-jumble',
//  masterKey: 'fbu-2018-key',
//  push: {
//    android: {
//      apiKey: 'AAAAto1Azj4:APA91bFn1oH_hLJClj29LIxz_M6S9JsKmF-Kw5THpqTNZiQhwJKmahv3CEmdx6SefV9scaLUV0eUcIdzsHHYjSnKl10ufo6yN-IDQnRw4z9UFF7sysn2E_7oZPKGPjQzJFMU3sEXKNEWAxY3ofiLJ9R3rK6Ht94OVA' // The Server API Key of FCM
//    }
//  }
//});

var api = new ParseServer({
  push: pushConfig,
  databaseURI: databaseUri || 'mongodb://heroku_86l8nj5d:nthvbkpajohdnjqtbd2vjspvbv@ds111562.mlab.com:11562/heroku_86l8nj5d',
  cloud: process.env.CLOUD_CODE_MAIN || __dirname + '/cloud/main.js',
  appId: process.env.APP_ID || 'fbu-2018',
  masterKey: process.env.MASTER_KEY || 'fbu-2018-key', //Add your master key here. Keep it secret!
  serverURL: process.env.SERVER_URL || 'http://fbu-2018.herokuapp.com/parse',  // Don't forget to change to https if needed
  liveQuery: {
    classNames: ["Posts", "Comments"] // List of classes to support for query subscriptions
  }
});

// Client-keys like the javascript key or the .NET key are not necessary with parse-server
// If you wish you require them, you can set them as options in the initialization above:
// javascriptKey, restAPIKey, dotNetKey, clientKey

var app = express();

// Serve static assets from the /public folder
app.use('/public', express.static(path.join(__dirname, '/public')));

// Serve the Parse API on the /parse URL prefix
var mountPath = process.env.PARSE_MOUNT || '/parse';
app.use(mountPath, api);

// Parse Server plays nicely with the rest of your web routes
app.get('/', function(req, res) {
  res.status(200).send('I dream of being a website.  Please star the parse-server repo on GitHub!');
});

// There will be a test page available on the /test path of your server url
// Remove this before launching your app
app.get('/test', function(req, res) {
  res.sendFile(path.join(__dirname, '/public/test.html'));
});

var port = process.env.PORT || 1337;
var httpServer = require('http').createServer(app);
httpServer.listen(port, function() {
    console.log('parse-server-example running on port ' + port + '.');
});

// This will enable the Live Query real-time server
ParseServer.createLiveQueryServer(httpServer);
