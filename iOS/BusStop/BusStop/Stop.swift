//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

class Stop {
    
    var stopNumber: Int
    var stopName: String
    var routeNumber: Int
    var routeName: String
    
    init(stopNumber: Int, stopName : String, routeNumber: Int, routeName: String) {
        self.stopNumber = stopNumber
        self.stopName = stopName
        self.routeNumber = routeNumber
        self.routeName = routeName
    }
}
