//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

class Stop {
    
    var stopNumber: String
    var stopName: String
    var routeNumber: String
    var routeName: String
    
    init(stopNumber: String, stopName : String, routeNumber: String, routeName: String) {
        self.stopNumber = stopNumber
        self.stopName = stopName
        self.routeNumber = routeNumber
        self.routeName = routeName
    }
}
