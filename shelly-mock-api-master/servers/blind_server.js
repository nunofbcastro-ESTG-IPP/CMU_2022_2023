var express = require('express');
var logger = require('morgan');

var ip = require('ip');
const Blind = require('../models/blind');
const isInt = require('../utils').isInt;

function createBlindServer(port) {
  var app = express();
  app.use(logger('dev'));
  app.use(express.json());
  app.use(express.urlencoded({ extended: false }));

  const blind = new Blind(ip.address());

  app.get('/status', (req, res) => {
    res.json(blind.state);
  });

  app.get('/meters/:meter_id', (req, res) => {
    const _id = req.params.meter_id;
    if (isInt(_id)) {
      const id = parseInt(_id);
      if (id >= 0 && id <= 1) {
        res.json(blind.state.meters[id]);
      }
    }
  });
  app.get('/roller/0', (req, res) => {
    const go = req.query.go;
    const roller_pos = req.query.roller_pos;
    //not support yet! use to_pos option
    //const duration = req.query.duration
    //const offset = req.query.duration
    switch (go) {
      case 'open': {
        blind.open();
        break;
      }
      case 'close': {
        blind.close();
        break;
      }
      case 'stop': {
        blind.stop();
        break;
      }
      case 'to_pos': {
        blind.to_pos(roller_pos);
        break;
      }
      default: {
        break;
      }
    }
    res.json(blind.state.rollers[0]);
  });

  app.use(function (err, req, res, next) {
    res.json(err.message);
  });

  app.listen(port, (err) => {
    if (err) console.log(err);
    console.log('blind server listening on PORT ', port);
  });
}

module.exports = createBlindServer;
