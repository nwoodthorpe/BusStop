//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

struct Stop {
    
    var stopNumber: Int
    var stopName: String
    var routeNumber: Int
    var routeName: String
    var time: Int
    var nickname: String
    
    init(stopNumber: Int, stopName : String, routeNumber: Int, routeName: String, time: Int, nickname: String) {
        self.stopNumber = stopNumber
        self.stopName = stopName
        self.routeNumber = routeNumber
        self.routeName = routeName
        self.time = time
        self.nickname = nickname
    }
}
