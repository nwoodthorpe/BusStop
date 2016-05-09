//
//  savedStop.swift
//  BusStop
//
//  Created by Harry Liu on 2016-05-01.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

public class savedStop: Stop {
    
    public var time: Int
    public var nickname: String
    public var on: Bool
    
    public init(stopNumber: String, stopName : String, routeNumber: String, routeName: String, time: Int, nickname: String, on: Bool) {
        self.time = time
        self.nickname = nickname
        self.on = on
        super.init(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
    }
    
    public init(stop: Stop, nickname: String) {
        self.time = -1
        self.nickname = nickname
        self.on = true
        super.init(stopNumber: stop.stopNumber, stopName: stop.stopName, routeNumber: stop.routeNumber, routeName: stop.routeName)
    }
}
