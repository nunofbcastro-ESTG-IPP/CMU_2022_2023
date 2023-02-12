const {getRndInteger, getRndString, getDate} =require( "../utils");

class Blind {
    constructor(ip){
        this.state = {
            wifi_sta: {
                connected: true,
                ssid: "ShellyWifi" + getRndInteger(0,1000),
                ip: ip,
                rssi: - getRndInteger(40,100),
            },
            cloud: {
                enabled: false,
                connected: false
            },
            mqtt: {
                connected: false
            },
            time: getDate(),
            unixtime: 1668592778,
            serial: 53,
            has_update: false,
            mac: getRndString(12),
            cfg_changed_cnt: 0,
            actions_stats: {
                skipped: 0
            },
            rollers: [
                {
                    state: "stop",
                    source: "schedule",
                    power: 0.00,
                    is_valid: true,
                    safety_switch : false,
                    overtemperature: false,
                    stop_reason: "normal",
                    last_direction: "close",
                    current_pos: 0,
                    calibrating: false,
                    positioning: true
                }
            ],
            meters: [
                {
                    power: 0.00,
                    overpower: 0.00,
                    is_valid: true,
                    timestamp: 1668592778,
                    counters: [
                        0.000,
                        0.000,
                        0.000
                    ],
                    total: 645
                },
                {
                    power: 0.00,
                    overpower: 0.00,
                    is_valid: true,
                    timestamp: 1668592778,
                    counters: [
                        0.000,
                        0.000,
                        0.000
                    ],
                    total: 306
                }
            ],
            inputs: [
                {
                    input: 1,
                    event: "",
                    event_cnt: 0
                },
                {
                    input: 0,
                    event: "",
                    event_cnt: 0
                }
            ],
            temperature: 47.19,
            overtemperature: false,
            tmp: {
                tC: 47.19,
                tF: 116.95,
                is_valid: true
            },
            temperature_status: "Normal",
            update: {
                status: "unknown",
                has_update: false,
                new_version: "",
                old_version: "20221027-092056/v1.12.1-ga9117d3"
            },
            ram_total: 50728,
            ram_free: 37608,
            fs_size: 233681,
            fs_free: 145580,
            voltage: 241.57,
            uptime: 242601
        }
    }

    move(pos){
        const roller_pos = this.state.rollers[0].current_pos
        if ( this.state.rollers[0].state=="close" || this.state.rollers[0].state=="open"){
            clearInterval(this.simulateMovement)
        }
        this.simulateMovement = setInterval(()=>{
            const roller_pos = this.state.rollers[0].current_pos            
            if (roller_pos == pos || 
                (roller_pos < pos && this.state.rollers[0].state=="close") ||
                (roller_pos > pos && this.state.rollers[0].state=="open") ){
                clearInterval(this.simulateMovement)
                this.state.rollers[0].last_direction = this.state.rollers[0].state
                this.state.rollers[0].state="stop"
            }
            if (this.state.rollers[0].state=="open"){
                this.state.rollers[0].current_pos++
                this.state.meters[0].power = 163
                this.state.meters[0].total += 0.10
            }
            if (this.state.rollers[0].state=="close"){
                this.state.rollers[0].current_pos--
                this.state.meters[1].power = 83
                this.state.meters[1].total += 0.05
            }
        },500)
    }

    open(){
        this.state.rollers[0].state="open"
        this.move(100)
    }

    close(){
        this.state.rollers[0].state="close"
        this.move(0)
    }

    stop(){
        if (this.state.rollers[0].state!="stop"){
            clearInterval(this.simulateMovement)
            this.state.rollers[0].last_direction = this.state.rollers[0].state
            this.state.rollers[0].state="stop"
        }
    }

    to_pos(pos){
        if (this.state.rollers[0].current_pos > pos){
            this.state.rollers[0].state="close"
        }
        if (this.state.rollers[0].current_pos < pos){
            this.state.rollers[0].state="open"
        }
        this.move(pos)
    }
}


module.exports = Blind