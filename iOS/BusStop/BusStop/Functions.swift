//
//  Functions.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-29.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class Functions {
    
    static func stopToDict(stop: Stop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = stop.stopNumber
        dict["stopName"] = stop.stopName
        dict["routeNumber"] = stop.routeNumber
        dict["routeName"] = stop.routeName
        dict["nickname"] = stop.nickname
        
        return dict
    }
    
    static func dictToStop(dict: [String: AnyObject]) -> Stop {
        let stop = Stop(stopNumber: dict["stopNumber"] as! Int, stopName: dict["stopName"] as! String, routeNumber: dict["routeNumber"] as! Int, routeName: dict["routeName"] as! String, time: 99, nickname: dict["nickname"] as! String)
        
        return stop
    }
}
