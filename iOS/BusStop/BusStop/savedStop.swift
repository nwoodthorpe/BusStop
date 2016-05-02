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
    
    init(stopNumber: Int, stopName : String, routeNumber: Int, routeName: String, time: Int, nickname: String) {
        super.init(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
        self.time = time
        self.nickname = nickname
    }
    
    init(stop: Stop, nickname: String) {
        super.init(stopNumber: stop.stopNumber, stopName: stop.stopName, routeNumber: stop.routeNumber, routeName: stop.routeName)
        self.time = 0
        self.nickname = nickname
    }
}
