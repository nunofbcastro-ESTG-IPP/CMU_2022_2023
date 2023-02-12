var express = require('express');
var logger = require('morgan');

var ip = require('ip');
const Plug = require('../models/plug');
const isInt = require('../utils').isInt;

function createPlugServer(port) {
  var app = express();
  app.use(logger('dev'));
  app.use(express.json());
  app.use(express.urlencoded({ extended: false }));

  const plug = new Plug(ip.address());

  app.get('/status', (req, res) => {
    res.json(plug.state);
  });

  app.get('/meters/:meter_id', (req, res) => {
    const _id = req.params.meter_id;
    if (isInt(_id)) {
      const id = parseInt(_id);
      if (id == 0) {
        res.json(plug.state.meters[id]);
      }
    }
  });
  app.get('/relay/0', (req, res) => {
    const turn = req.query.turn;
    const timer = req.query.timer;
    switch (turn) {
      case 'on': {
        plug.turn('on', timer);
        break;
      }
      case 'off': {
        plug.turn('off', timer);
        break;
      }
      case 'toggle': {
        if (plug.state.relays[0].ison) {
          plug.turn('on', timer);
          break;
        }
        plug.turn('off');
        break;
      }
      default: {
        break;
      }
    }
    res.json(plug.state.relays[0]);
  });

  app.use(function (err, req, res, next) {
    res.json(err.message);
  });

  app.listen(port, (err) => {
    if (err) console.log(err);
    console.log('Plug listening on PORT', port);
  });
}
module.exports = createPlugServer;
