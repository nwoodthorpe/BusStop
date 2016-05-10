//
//  Functions.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-29.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

public class Functions {
    
    public static func stopToDict(s: Stop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = s.stopNumber
        dict["stopName"] = s.stopName
        dict["routeNumber"] = s.routeNumber
        dict["routeName"] = s.routeName
        
        return dict
    }
    
    public static func dictToStop(dict: [String: AnyObject]) -> Stop {
        let stopNumber = dict["stopNumber"] as! String
        let stopName = dict["stopName"] as! String
        let routeNumber = dict["routeNumber"] as! String
        let routeName = dict["routeName"] as! String
        let stop = Stop(stopNumber: stopNumber, stopName: stopName, routeNumber: routeNumber, routeName: routeName)
        
        return stop
    }
    
    public static func savedStopToDict(ss: savedStop) -> [String: AnyObject] {
        var dict = [String: AnyObject]()
        dict["stopNumber"] = ss.stopNumber
        dict["stopName"] = ss.stopName
        dict["routeNumber"] = ss.routeNumber
        dict["routeName"] = ss.routeName
        dict["nickname"] = ss.nickname
        dict["on"] = ss.on
        dict["absolutetime"] = CFAbsoluteTimeGetCurrent() + Double(ss.time)
        
        return dict
    }
    
    public static func dictToSavedStop(dict: [String: AnyObject]) -> savedStop {
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
    
    public static func createSavable(stops: [savedStop]) -> [[String: AnyObject]] {
        var array = [[String: AnyObject]]()
        for currentStop in stops {
            array.append(Functions.savedStopToDict(currentStop))
        }
        return array
    }
    
    public static func update(stops: [savedStop]) -> [savedStop] {
        for (index,currentStop) in stops.enumerate() {
            if !currentStop.on {
                continue
            }
            if let url = NSURL(string: "http://nwoodthorpe.com/grt/V2/livetime.php?stop=\(currentStop.stopNumber)"), contents = NSData(contentsOfURL: url) {
                
                do {
                    let object = try NSJSONSerialization.JSONObjectWithData(contents, options: .AllowFragments)
                    if let dictionary = object as? [String: [AnyObject]] {
                        for element in dictionary["data"]! {
                            if currentStop.routeNumber == element["routeId"] as? NSString {
                                stops[index].time = (element["departure"] as! NSNumber).integerValue - (element["time"] as! NSNumber).integerValue
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
