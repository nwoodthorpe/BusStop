//
//  savedStop.swift
//  BusStop
//
//  Created by Harry Liu on 2016-05-01.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

class savedStop: Stop {
    
    var time: Int
    var nickname: String
    var on: Bool
    
    init(stopNumber: String, stopName : String, routeNumber: String, routeName: String, time: Int, nickname: String, on: Bool) {
        self.time = time
        self.nickname = nickname
        self.on = on
        super.init(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
    }
    
    init(stop: Stop, nickname: String) {
        self.time = 0
        self.nickname = nickname
        self.on = true
        super.init(stopNumber: stop.stopNumber, stopName: stop.stopName, routeNumber: stop.routeNumber, routeName: stop.routeName)
    }
}
