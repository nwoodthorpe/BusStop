//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

open class Stop {
    
    open var stopNumber: String
    open var stopName: String
    open var routeNumber: String
    open var routeName: String
    
    public init(stopNumber: String, stopName : String, routeNumber: String, routeName: String) {
        self.stopNumber = stopNumber
        self.stopName = stopName
        self.routeNumber = routeNumber
        self.routeName = routeName
    }
}
