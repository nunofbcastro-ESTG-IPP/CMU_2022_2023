const { getRndInteger, getRndString, getDate } = require('../utils');

class Light {
  constructor(ip) {
    this.state = {
      wifi_sta: {
        connected: true,
        ssid: 'ShellyWifi' + getRndInteger(0, 1000),
        ip: ip,
        rssi: -getRndInteger(40, 100),
      },
      cloud: { enabled: true, connected: false },
      mqtt: { connected: false },
      time: '',
      unixtime: 0,
      serial: 1,
      has_update: false,
      mac: getRndString(12),
      cfg_changed_cnt: 0,
      actions_stats: { skipped: 0 },
      lights: [
        {
          ison: false,
          source: 'http',
          has_timer: false,
          timer_started: 0,
          timer_duration: 0,
          timer_remaining: 0,
          mode: 'color',
          red: 255,
          green: 127,
          blue: 0,
          white: 0,
          gain: 100,
          temp: 5406,
          brightness: 90,
          effect: 0,
          transition: 500,
        },
      ],
      meters: [
        {
          power: 0,
          is_valid: true,
          timestamp: 0,
          counters: [0.115, 0.115, 0.115],
          total: 4,
        },
      ],
      temperature: 19.96,
      overtemperature: false,
      tmp: { tC: 19.96, tF: 67.94, is_valid: true },
      update: {
        status: 'unknown',
        has_update: false,
        new_version: '',
        old_version: '20220809-124506/v1.12-g99f7e0b',
      },
      ram_total: 52072,
      ram_free: 39040,
      fs_size: 233681,
      fs_free: 167166,
      uptime: 97,
    };
  }

  turn(
    input,
    red = this.state.lights[0].red,
    green = this.state.lights[0].green,
    blue = this.state.lights[0].blue,
    white = this.state.lights[0].white,
    brightness = this.state.lights[0].brightness,
    gain = this.state.lights[0].gain,
    timer
  ) {
    const started = this.state.lights[0].ison;
    if (input != started) {
      if (input == 'on') {
        this.state.lights[0].ison = true;
        this.state.lights[0].red = red;
        this.state.lights[0].green = green;
        this.state.lights[0].blue = blue;
        this.state.lights[0].white = white;
        this.state.lights[0].brightness = brightness;
        this.state.lights[0].gain = gain;
        if (timer != undefined && timer != null && isInt(timer)) {
          const timeDuration = parseInt(timer);
          const timeStart = getUnixTimeStamp();
          this.state.lights[0].has_timer = true;
          this.state.lights[0].timer_duration = timeDuration;
          this.state.lights[0].timer_started = timeStart;
          this.state.lights[0].timer_remaining = timeDuration;
        }
        this.simulateConsumption = setInterval(() => {
          const consumption = getRndInteger(100, 250);
          this.state.meters[0].power = consumption;
          this.state.meters[0].total += (consumption * 3600) / 0.5;

          if (this.state.lights[0].has_timer) {
            const currentTime = getUnixTimeStamp();
            if (
              currentTime >
              this.state.lights[0].timer_started +
                this.state.lights[0].timer_duration
            ) {
              clearInterval(this.simulateConsumption);
              this.state.lights[0].ison = false;
              this.state.lights[0].timer_remaining = 0;
              this.state.lights[0].timer_started = 0;
              this.state.lights[0].timer_duration = 0;
              return;
            }
            this.state.lights[0].timer_remaining =
              this.state.lights[0].timer_started +
              this.state.lights[0].timer_duration -
              currentTime;
          }
        }, 500);
      }
      if (input == 'off') {
        clearInterval(this.simulateConsumption);
        this.state.lights[0].timer_remaining = 0;
        this.state.lights[0].timer_started = 0;
        this.state.lights[0].timer_duration = 0;
        this.state.lights[0].ison = false;
      }
    }
  }
}

module.exports = Light;
