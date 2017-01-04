//
//  Functions.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-29.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

open class Functions {
    
    open static func stopToDict(_ s: Stop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = s.stopNumber as AnyObject?
        dict["stopName"] = s.stopName as AnyObject?
        dict["routeNumber"] = s.routeNumber as AnyObject?
        dict["routeName"] = s.routeName as AnyObject?
        
        return dict
    }
    
    open static func dictToStop(_ dict: [String: AnyObject]) -> Stop {
        let stopNumber = dict["stopNumber"] as! String
        let stopName = dict["stopName"] as! String
        let routeNumber = dict["routeNumber"] as! String
        let routeName = dict["routeName"] as! String
        let stop = Stop(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
        
        return stop
    }
    
    open static func savedStopToDict(_ ss: savedStop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = ss.stopNumber as AnyObject?
        dict["stopName"] = ss.stopName as AnyObject?
        dict["routeNumber"] = ss.routeNumber as AnyObject?
        dict["routeName"] = ss.routeName as AnyObject?
        dict["nickname"] = ss.nickname as AnyObject?
        dict["on"] = ss.on as AnyObject?
        dict["absolutetime"] = CFAbsoluteTimeGetCurrent() + Double(ss.time) as AnyObject?
        
        return dict
    }
    
    open static func dictToSavedStop(_ dict: [String: AnyObject]) -> savedStop {
        let stopNumber = dict["stopNumber"] as! String
        let stopName = dict["stopName"] as! String
        let routeNumber = dict["routeNumber"] as! String
        let routeName = dict["routeName"] as! String
        let nickname = dict["nickname"] as! String
        let on = dict["on"] as! Bool
        let time = Int(dict["absolutetime"] as! Double - CFAbsoluteTimeGetCurrent())
        let stop = savedStop(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName, time: time, nickname: nickname, on: on)
        
        return stop
    }
    
    open static func createSavable(_ stops: [savedStop]) -> [[String: AnyObject]] {
        var array = [[String: AnyObject]]()
        for currentStop in stops {
            array.append(Functions.savedStopToDict(currentStop))
        }
        return array
    }
    
    open static func update(_ stops: [savedStop]) -> [savedStop] {
        for (index,currentStop) in stops.enumerated() {
            if !currentStop.on {
                continue
            }
            if let url = URL(string: "http://nwoodthorpe.com/grt/V2/livetime.php?stop=\(currentStop.stopNumber)"), let contents = try? Data(contentsOf: url) {
                
                do {
                    let object = try JSONSerialization.jsonObject(with: contents, options: .allowFragments)
                    if let dictionary = object as? [String: [AnyObject]] {
                        for element in dictionary["data"]! {
                            if currentStop.routeNumber == element["routeId"] as? String {
                                stops[index].time = (element["departure"] as! NSNumber).intValue - (element["time"] as! NSNumber).intValue
                            }
                        }
                    }
                    else {
                        print("JSON failed to parse: Stop \(currentStop.nickname)")
                    }
                }
                catch {
                    print("handle error")
                }
            }
        }
        return stops
    }
}
