//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
exports.sendPushNotification = functions.database
  .ref('/Apartamentos/{apartmentId}')
  .onWrite(event => {
    const payload = {
      notification: {
        tittle: 'Notificación Appartacho',
        body: 'Se ha agregado un nuevo apartamento',
        badge: '1',
        sound: 'default'
      }
    };
    return admin.database().ref('Devices').once('value').then(allToken => {
      if (allToken.val()) {
        const token = Object.keys(allToken.val());
        return admin.messaging().sendToDevice(token, payload).then(response => {});
      };
    });
  });
