//
//  Functions.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-29.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class Functions {
    
    static func stopToDict(s: Stop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = s.stopNumber
        dict["stopName"] = s.stopName
        dict["routeNumber"] = s.routeNumber
        dict["routeName"] = s.routeName
        
        return dict
    }
    
    static func dictToStop(dict: [String: AnyObject]) -> Stop {
        let stopNumber = dict["stopNumber"] as! String
        let stopName = dict["stopName"] as! String
        let routeNumber = dict["routeNumber"] as! String
        let routeName = dict["routeName"] as! String
        let stop = Stop(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
        
        return stop
    }
    
    static func savedStopToDict(ss: savedStop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = ss.stopNumber
        dict["stopName"] = ss.stopName
        dict["routeNumber"] = ss.routeNumber
        dict["routeName"] = ss.routeName
        dict["nickname"] = ss.nickname
        dict["on"] = ss.on
        
        return dict
    }
    
    static func dictToSavedStop(dict: [String: AnyObject]) -> savedStop {
        let stopNumber = dict["stopNumber"] as! String
        let stopName = dict["stopName"] as! String
        let routeNumber = dict["routeNumber"] as! String
        let routeName = dict["routeName"] as! String
        let nickname = dict["nickname"] as! String
        let on = dict["on"] as! Bool
        let stop = savedStop(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName, time: 0, nickname: nickname, on: on)
        
        return stop
    }
}
