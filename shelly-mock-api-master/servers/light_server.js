var express = require('express');
var logger = require('morgan');

var ip = require('ip');
const Light = require('../models/light');
const isInt = require('../utils').isInt;

function createLightServer(port) {
  var app = express();
  app.use(logger('dev'));
  app.use(express.json());
  app.use(express.urlencoded({ extended: false }));

  const light = new Light(ip.address());

  app.get('/status', (req, res) => {
    res.json(light.state);
  });

  app.get('/meters/:meter_id', (req, res) => {
    const _id = req.params.meter_id;
    if (isInt(_id)) {
      const id = parseInt(_id);
      if (id == 0) {
        res.json(light.state.meters[id]);
      }
    }
  });
  app.get('/light/0', (req, res) => {
    const turn = req.query.turn;
    const value_red = req.query.red;
    const value_blue = req.query.blue;
    const value_green = req.query.green;
    const value_white = req.query.white;
    const value_brightness = req.query.brightness;
    const value_gain = req.query.gain;
    const value_timer = req.query.timer;

    switch (turn) {
      case 'on': {
        if (
          value_red != null &&
          value_red != undefined &&
          value_blue != null &&
          value_blue != undefined &&
          value_green != null &&
          value_green != undefined &&
          value_gain != null &&
          value_gain != undefined
        ) {
          light.turn(
            (input = 'on'),
            (red = value_red),
            (green = value_green),
            (blue = value_blue),
            (white = undefined),
            (brightness = undefined),
            (gain = value_gain),
            (timer = value_timer)
          );
          break;
        }
        if (
          value_red != null &&
          value_red != undefined &&
          value_blue != null &&
          value_blue != undefined &&
          value_green != null &&
          value_green != undefined
        ) {
          light.turn(
            (input = 'on'),
            (red = value_red),
            (green = value_green),
            (blue = value_blue),
            (white = undefined),
            (brightness = undefined),
            (gain = undefined),
            (timer = value_timer)
          );
          break;
        }
        if (
          value_white != null &&
          value_white != undefined &&
          value_brightness != null &&
          value_brightness != undefined
        ) {
          light.turn(
            'on',
            undefined,
            undefined,
            undefined,
            value_white,
            value_brightness,
            undefined,
            value_timer
          );
          break;
        }
        if (value_white != null && value_white != undefined) {
          light.turn(
            (input = 'on'),
            (red = undefined),
            (green = undefined),
            (blue = undefined),
            (white = value_white),
            (brightness = undefined),
            (gain = undefined),
            (timer = value_timer)
          );
          break;
        }
        light.turn(
          (input = 'on'),
          (red = undefined),
          (green = undefined),
          (blue = undefined),
          (white = undefined),
          (brightness = undefined),
          (gain = undefined),
          (timer = undefined)
        );
        break;
      }
      case 'off': {
        light.turn(
          (input = 'off'),
          (red = undefined),
          (green = undefined),
          (blue = undefined),
          (white = undefined),
          (brightness = undefined),
          (gain = undefined),
          (timer = value_timer)
        );
        break;
      }
      case 'toggle': {
        if (light.state.relays[0].ison) {
          light.turn(
            (input = 'on'),
            (red = undefined),
            (green = undefined),
            (blue = undefined),
            (white = undefined),
            (brightness = undefined),
            (gain = undefined),
            (timer = value_timer)
          );
          break;
        }
        light.turn(
          (input = 'off'),
          (red = undefined),
          (green = undefined),
          (blue = undefined),
          (white = undefined),
          (brightness = undefined),
          (gain = undefined),
          (timer = undefined)
        );
        break;
      }
      default: {
        break;
      }
    }
    res.json(light.state.lights[0]);
  });

  app.use(function (err, req, res, next) {
    res.json(err.message);
  });

  app.listen(port, (err) => {
    if (err) console.log(err);
    console.log('light listening on PORT', port);
  });
}

module.exports = createLightServer;
