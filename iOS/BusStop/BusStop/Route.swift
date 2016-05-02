//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

struct Route {
    
    var Number: String
    var Name: String
    var Text: String
    var Direction: String
    var DirectionName: String
    var Stops: [String]
    
    init(number: String, name: String, text: String, direction: String, directionName: String, stops: [String]) {
        Number = number
        Name = name
        Text = text
        Direction = direction
        DirectionName = directionName
        Stops = stops
    }
    
}